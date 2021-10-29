package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Main;
<<<<<<< Updated upstream
import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.message.CheckMessage;
import formet.message.History;
import formet.message.LoginRequest;
=======
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.message.CheckMessage;
import format.message.CoinTypeChange;
import format.message.History;
import format.message.LoginCheckMessage;
import format.message.LoginRequest;
>>>>>>> Stashed changes
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.StageControll;
import util.Util;

public class Client {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private History lastHistoryData = null;
	private AnchorPane currentRoot;
<<<<<<< Updated upstream
=======

	private String currentCoinId = "양디코인";
	private String currentHistoryBlockType = "minute";
>>>>>>> Stashed changes
	
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
	
	public void SendObject(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readData() {
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
					if(((CheckMessage) objectMsg).check) {
						sc.newStage("/view/fxml/Main.fxml", this.currentRoot, this, true);
						util.alert("안내", "성공", ((CheckMessage) objectMsg).msg);
					} else {
						util.alert("안내", "실패", ((CheckMessage) objectMsg).msg);
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
	
<<<<<<< Updated upstream
=======
	public void changeHistoryBlock(String blockType) {
		this.currentHistoryBlockType = blockType;
		System.out.println(currentHistoryBlockType);
		CoinTypeChange();
	}
	
	public void changeCoinType(String coinId) {
		this.currentCoinId = coinId;
		CoinTypeChange();
	}
	
	private void CoinTypeChange() {
		SendObject(new CoinTypeChange(this.currentCoinId, this.currentHistoryBlockType));
	}
	
	public void SendObject(Object obj) {
		System.out.println(((MessageObject) obj).type);
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
>>>>>>> Stashed changes
	public History getHistory() {
		return this.lastHistoryData;
	}
	
	public void setRoot(AnchorPane root) {
		this.currentRoot = root;
	}
}
