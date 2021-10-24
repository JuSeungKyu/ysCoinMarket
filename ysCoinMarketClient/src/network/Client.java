package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.message.CheckMessage;
import formet.message.History;
import formet.message.LoginRequest;
import javafx.scene.layout.AnchorPane;
import util.Util;

public class Client {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private History lastHistoryData;
	private AnchorPane currentRoot;
	
	public Client() {
		try {
			Socket socket = new Socket("127.0.0.1", 2657);
			
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					readData();
				}
			}).start();
			
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
		MessageObject objectMsg = null;
		while(true) {
			try {
				objectMsg = (MessageObject) this.ois.readObject();
				
				
				if(objectMsg.type == MessageTypeConstantNumbers.HISTORY_LIST) {
					this.lastHistoryData = (History) objectMsg;
				}
				
				if(objectMsg.type == MessageTypeConstantNumbers.CHECK_MSG) {
					if(((CheckMessage) objectMsg).check) {
						util.alert("안내", "성공", ((CheckMessage) objectMsg).msg);
						util.newStage("/view/fxml/Main.fxml", this.currentRoot);
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
			util.sleep(100);
		}
	}
	
	public History getHistory() {
		return this.lastHistoryData;
	}
	
	public void setRoot(AnchorPane root) {
		this.currentRoot = root;
	}
}
