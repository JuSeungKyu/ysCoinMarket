package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import application.Main;
import format.HistoryInfo;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TypeInfo;
import format.message.CheckMessage;
import format.message.History;
import format.message.LoginCheckMessage;
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
	private String currentHistoryBlockType = "minute";
	private TypeInfo[] typeInfoList;
	private Util util = new Util();
	
	private ArrayList<MessageObject> sendMsgList = new ArrayList<MessageObject>();

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
			while(sendMsgList.size() != 0) {
				this.SendObject(sendMsgList.get(0));
				sendMsgList.remove(0);
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

				if (objectMsg.type == MessageTypeConstantNumbers.HISTORY_LIST) {
					this.lastHistoryData = (History) objectMsg;
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
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

//			if (!util.sleep(100)) {
//				return;
//			}
		}
	}

	public TypeInfo[] getTypeInfo() {
		return this.typeInfoList;
	}
	
	public void changeHistoryBlock(String blockType) {
		this.currentHistoryBlockType = blockType;
		System.out.println(currentHistoryBlockType);
		CoinTypeChange();
	}

	public void changeCoinType(String coinId) {
		this.currentCoinId = coinId;
		CoinTypeChange();
	}

	public void changeGraphRange(short[] range) {
		SendObject(new UpdateGraphRange(range));
	}

	private void CoinTypeChange() {
		SendObject(new CoinTypeChange(this.currentCoinId, this.currentHistoryBlockType));
	}

	private void SendObject(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addSendObject(Object obj) {
		this.sendMsgList.add((MessageObject) obj);
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

	public void setHistoryInfoData(HistoryInfo historyInfoData) {
		HistoryInfoData = historyInfoData;
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

	public String getCurrentCoinId() {
		return currentCoinId;
	}

	public void setCurrentCoinId(String currentCoinId) {
		this.currentCoinId = currentCoinId;
	}

	public String getCurrentHistoryBlockType() {
		return currentHistoryBlockType;
	}

	public void setCurrentHistoryBlockType(String currentHistoryBlockType) {
		this.currentHistoryBlockType = currentHistoryBlockType;
	}

	public byte getCurrentCoinDifficulty() {
		return currentCoinDifficulty;
	}

	public void setCurrentCoinDifficulty(byte currentCoinDifficulty) {
		this.currentCoinDifficulty = currentCoinDifficulty;
	}

	public TypeInfo[] getTypeInfoList() {
		return typeInfoList;
	}

	public void setTypeInfoList(TypeInfo[] typeInfoList) {
		this.typeInfoList = typeInfoList;
	}
}
