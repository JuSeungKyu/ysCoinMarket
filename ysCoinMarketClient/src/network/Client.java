package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import application.Main;
import format.Bool;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;
import format.TypeInfo;
import format.message.CheckMessage;
import format.message.History;
import format.message.LoginCheckMessage;
import format.message.LoginRequest;
import format.message.MineBlockRequest;
import format.message.PreviousHashMessage;
import format.message.TransactionDetailsMessage;
import format.message.TypeInfoUpdate;
import format.message.UpdateGraphRange;
import format.message.UpdateOrderBook;
import format.message.UserInfoMsg;
import format.message.CoinTypeChange;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.StageControll;
import util.Util;
import util.uiUpdate.UIUpdateClass;
import util.uiUpdate.UIUpdateThread;

public class Client {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private History lastHistoryData = null;
	private AnchorPane currentRoot;
	private ArrayList<TransactionDetailsInfo> transactionDetailsInfo = null;

	protected String currentCoinId = "양디코인";
	private byte currentCoinDifficulty = 3;
	private String hash = null;
	private TypeInfo[] typeInfoList = new TypeInfo[0];

	private int money = 0;
	private int coinCount = 0;
	
	private ArrayList<int[]> orderBook = null;

	private Util util = new Util();

	private Queue<MessageObject> sendMsgQueue = new LinkedList<MessageObject>();

	private Bool loginCheck;
	
	public Client(Bool loginCheck) {
		try {
			this.loginCheck = loginCheck;
			
			Socket socket = new Socket("127.0.0.1", 2657);
			Main.socket = socket;
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());

			Thread readObjectThread = new Thread(() -> {
				readData();
			});
			Thread writeObjectThread = new Thread(() -> {
				writeData();
			});
			Main.ThreadList.add(readObjectThread);
			Main.ThreadList.add(writeObjectThread);
			readObjectThread.start();
			writeObjectThread.start();

		} catch (UnknownHostException e) {
			util.alert("경고", "서버에 연결할 수 없습니다", "연결 상태를 확인해주세요");
			this.loginCheck.setValue(false);
		} catch (IOException e) {
			util.alert("경고", "서버에 연결할 수 없습니다", "연결 상태를 확인해주세요");
			this.loginCheck.setValue(false);
		}
	}

	private void writeData() {
		while (true) {
			while (sendMsgQueue.size() != 0) {
				this.sendObject(sendMsgQueue.poll());
				sendMsgQueue.remove(0);
			}
			if(!util.sleep(10)) {
				return;
			}
		}
	}

	private void readData() {
		MessageObject objectMsg = null;
		while (true) {
			try {
				objectMsg = (MessageObject) this.ois.readObject();

				if (objectMsg == null) {
					System.out.println("null 메시지");
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.HISTORY_LIST) {
					this.lastHistoryData = (History) objectMsg;
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.UPDATE_TYPE_INFO) {
					this.typeInfoList = ((TypeInfoUpdate) objectMsg).info;
					continue;
				}
				
				if (objectMsg.type == MessageTypeConstantNumbers.UPDATE_ORDER_BOOK) {
					this.orderBook = ((UpdateOrderBook) objectMsg).info;
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.USER_INFO_MSG) {
					this.updateMoneyInfo((UserInfoMsg) objectMsg);
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.CHECK_MSG) {
					util.alert("안내", ((CheckMessage) objectMsg).check ? "성공" : "실패", ((CheckMessage) objectMsg).msg);
					this.justCheck = ((CheckMessage) objectMsg).check;
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.LOGIN_CHECK_MSG) {
					if (((LoginCheckMessage) objectMsg).check) {
						successLogin((LoginCheckMessage) objectMsg);
					} else {
						failLogin((LoginCheckMessage) objectMsg);
					}
					continue;
				}
				if (objectMsg.type == MessageTypeConstantNumbers.TRANSACTION_DETAILS_UPDATE) {
					transactionDetailsInfo = ((TransactionDetailsMessage) objectMsg).info;
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.PREVIOUS_HASH_MESSAGE) {
					this.hash = ((PreviousHashMessage) objectMsg).hash;
					continue;
				}

			} catch (ClassNotFoundException e) {
				break;
			} catch (IOException e) {
				break;
			}
		}
	}
	
	private void failLogin(LoginCheckMessage msg) {
		util.alert("안내", "실패", ((LoginCheckMessage) msg).msg);
		this.loginCheck.setValue(false);
	}
	
	private void successLogin(LoginCheckMessage msg) {
		StageControll sc = new StageControll();
		
		sc.newStage("/view/fxml/Main.fxml", this.currentRoot, this, true);
		util.alert("안내", "성공", ((LoginCheckMessage) msg).msg);
		this.loginCheck.setValue(false);
		this.loginCheck = null;
	}

	private void updateMoneyInfo(UserInfoMsg msg) {
		Label moneyLabel = (Label) this.currentRoot.getChildren()
				.get(util.getIndexById(this.currentRoot.getChildren(), "moneyLabel"));
		Label coinCountLabel = (Label) this.currentRoot.getChildren()
				.get(util.getIndexById(this.currentRoot.getChildren(), "coinCountLabel"));
		Label coinFeeLabel = (Label) this.currentRoot.getChildren()
				.get(util.getIndexById(this.currentRoot.getChildren(), "coinFeeLabel"));

		new UIUpdateClass() {
			@Override
			public void update() {
				moneyLabel.setText("사용 가능한 돈 : " + msg.money);
				coinCountLabel.setText("사용 가능 코인 : " + msg.count);
				coinFeeLabel.setText("매수 주문 수수료 : " + msg.fee + "%");
			}
		}.start();
	}

	public void addBlock(String hash) {
		addSendObject(new MineBlockRequest(hash, this.currentCoinId));
	}

	public TypeInfo[] getTypeInfo() {
		return this.typeInfoList;
	}
	public void changeGraphRange(short[] range) {
		sendObject(new UpdateGraphRange(range));
	}

	private void coinTypeChange() {
		sendObject(new CoinTypeChange(this.currentCoinId));
	}

	private void sendObject(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addSendObject(Object obj) {
		this.sendMsgQueue.add((MessageObject) obj);
	}

	public History getHistory() {
		return this.lastHistoryData;
	}

	public void setRoot(AnchorPane root) {
		this.currentRoot = root;
	}

	public ArrayList<TransactionDetailsInfo> getTransactionDetailsData() {
		ArrayList<TransactionDetailsInfo> info = (ArrayList<TransactionDetailsInfo>) this.transactionDetailsInfo.clone();
		this.transactionDetailsInfo = null;
		return info;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public History getLastHistoryData() {
		return lastHistoryData;
	}

	public void setLastHistoryData(History lastHistoryData) {
		this.lastHistoryData = lastHistoryData;
	}

	public AnchorPane getCurrentRoot() {
		return currentRoot;
	}

	public void setCurrentRoot(AnchorPane currentRoot) {
		this.currentRoot = currentRoot;
	}

	public void setCurrentCoinId(String currentCoinId) {
		this.currentCoinId = currentCoinId;
		
		Label coinTypeLabel = (Label) this.currentRoot.getChildren()
				.get(util.getIndexById(this.currentRoot.getChildren(), "coinTypeLabel"));
		
		new UIUpdateClass() {
			@Override
			public void update() {
				coinTypeLabel.setText("현재 종목 : " + currentCoinId);
			}
		}.start();
	}

	public String getCurrentCoinId() {
		return currentCoinId;
	}

	public byte getCurrentCoinDifficulty() {
		return currentCoinDifficulty;
	}

	public TypeInfo[] getTypeInfoList() {
		return typeInfoList;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	private boolean justCheck;

	public void justCheckStart() {
		this.justCheck = false;
	}

	public boolean justChecking() {
		return this.justCheck;
	}

	public int getMoney() {
		return this.money;
	}

	public int getCoinCount() {
		return this.coinCount;
	}
	
	public ArrayList<int[]> getOrderBook(){
		return this.orderBook;
	}
}
