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
import format.HistoryInfo;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TypeInfo;
import format.message.CheckMessage;
import format.message.History;
import format.message.LoginCheckMessage;
import format.message.TransactionDetailsMessage;
import format.message.TypeInfoUpdate;
import format.message.UpdateGraphRange;
import format.message.CoinTypeChange;
import javafx.scene.layout.AnchorPane;
import util.StageControll;
import util.Util;

public class Client {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private History lastHistoryData = null;
	private AnchorPane currentRoot;
	private HistoryInfo HistoryInfoData = null;

	private String currentCoinId = "양디코인";
	private byte currentCoinDifficulty = 1;
	private TypeInfo[] typeInfoList;
	private Util util = new Util();

	private Queue<MessageObject> sendMsgQueue = new LinkedList<MessageObject>();

	public Client() {
		try {
			Socket socket = new Socket("127.0.0.1", 2657);

			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());

			Thread readObjectThread = new Thread(()-> {
				readData();
			});
			Thread writeObjectThread = new Thread(()-> {
				writeData();
			});
			Main.ThreadList.add(readObjectThread);
			Main.ThreadList.add(writeObjectThread);
			readObjectThread.start();
			writeObjectThread.start();

		} catch (UnknownHostException e) {
			System.out.println("서버에 연결할 수 없습니다");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeData() {
		while(true) {
			while(sendMsgQueue.size() != 0) {
				this.sendObject(sendMsgQueue.poll());
				sendMsgQueue.remove(0);
			}
			util.sleep(10);
		}
	}

	private void readData() {
		StageControll sc = new StageControll();
		MessageObject objectMsg = null;
		while (true) {
			try {
				objectMsg = (MessageObject) this.ois.readObject();
				
				if(objectMsg == null) {
					System.out.println("null 메시지");
					continue;
				}

				System.out.println(objectMsg.type);
				
				if (objectMsg.type == MessageTypeConstantNumbers.HISTORY_LIST) {
					this.lastHistoryData = (History) objectMsg;
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.CHECK_MSG) {
					util.alert("안내", ((CheckMessage) objectMsg).check ? "성공" : "실패", ((CheckMessage) objectMsg).msg);
					continue;
				}

				if (objectMsg.type == MessageTypeConstantNumbers.LOGIN_CHECK_MSG) {
					if (((LoginCheckMessage) objectMsg).check) {
						sc.newStage("/view/fxml/Main.fxml", this.currentRoot, this, true);
						util.alert("안내", "성공", ((LoginCheckMessage) objectMsg).msg);
					} else {
						util.alert("안내", "실패", ((LoginCheckMessage) objectMsg).msg);
						break;
					}
					continue;
				}
				
				if(objectMsg.type == MessageTypeConstantNumbers.UPDATE_TYPE_INFO) {
					this.typeInfoList = ((TypeInfoUpdate)objectMsg).info;
					continue;
				}
				
				if(objectMsg.type == MessageTypeConstantNumbers.TRANSACTION_DETAILS_UPDATE) {
					System.out.println("hi");
					System.out.println(((TransactionDetailsMessage) objectMsg).info.toString());
					continue;
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public TypeInfo[] getTypeInfo() {
		return this.typeInfoList;
	}

	public void changeCoinType(String coinId) {
		this.currentCoinId = coinId;
		coinTypeChange();
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

	public HistoryInfo getHistoryInfoData() {
		return HistoryInfoData;
	}

	public String getCurrentCoinId() {
		return currentCoinId;
	}
	
	public byte getCurrentCoinDifficulty() {
		return currentCoinDifficulty;
	}
}
