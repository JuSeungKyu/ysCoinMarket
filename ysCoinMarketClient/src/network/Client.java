package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Main;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.message.CheckMessage;
import format.message.History;
import format.message.LoginCheckMessage;
import format.message.LoginRequest;
import format.message.UpdateGraphRange;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.message.CheckMessage;
import format.message.CoinTypeChange;
import format.message.History;
import format.message.LoginCheckMessage;
import format.message.LoginRequest;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.StageControll;
import util.Util;

public class Client {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private History lastHistoryData = null;
	private AnchorPane currentRoot;
	
	private String currentCoinId = "양디코인";
	private String currentHistoryBlockType = "minute";
	
	public Client() {
		try {
			Socket socket = new Socket("127.0.0.1", 2657);
			
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			
			Thread readObjectThread = new Thread(new Runnable() {
				@Override
				public void run() {
					readData();
				}
			});
			Main.ThreadList.add(readObjectThread);
			readObjectThread.start();
			
		} catch (UnknownHostException e) {
			System.out.println("서버에 연결할 수 없습니다");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readData() {
		Util util = new Util();
		StageControll sc = new StageControll();
		MessageObject objectMsg = null;
		while(true) {
			try {
				objectMsg = (MessageObject) this.ois.readObject();
				
				
				if(objectMsg.type == MessageTypeConstantNumbers.HISTORY_LIST) {
					this.lastHistoryData = (History) objectMsg;
				}
				
				if(objectMsg.type == MessageTypeConstantNumbers.CHECK_MSG) {
					util.alert("안내", ((CheckMessage) objectMsg).check ? "성공" : "실패", ((CheckMessage) objectMsg).msg);
					continue;
				}
				
				if(objectMsg.type == MessageTypeConstantNumbers.LOGIN_CHECK_MSG) {
					if(((LoginCheckMessage) objectMsg).check) {
						sc.newStage("/view/fxml/Main.fxml", this.currentRoot, this, true);
						util.alert("안내", "성공", ((LoginCheckMessage) objectMsg).msg);
					} else {
						util.alert("안내", "실패", ((LoginCheckMessage) objectMsg).msg);
						break;
					}
					continue;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!util.sleep(100)) {
				return;
			}
		}
	}
	
	public String getTypeDBName() {
		return null;//코인이름
		
	}
	
	public String getTypeDBNum() {
		return null;//코인값
		
	}
	public String getYesterdayCoin() {
		return null;//전날코인값
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
	
	public void SendObject(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public History getHistory() {
		return this.lastHistoryData;
	}
	
	public void setRoot(AnchorPane root) {
		this.currentRoot = root;
	}
}
