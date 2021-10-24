package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.message.CheckMessage;
import formet.message.LoginRequest;
import util.Util;

public class Client {
	public static ObjectOutputStream oos;
	public static ObjectInputStream ois;
	public Client(LoginRequest loginInfo) {
		try {
			Socket socket = new Socket("127.0.0.1", 2657);
			
			Client.oos = new ObjectOutputStream(socket.getOutputStream());
			Client.oos.writeObject(loginInfo);
			
			Client.ois = new ObjectInputStream(socket.getInputStream());
			new Thread(new ReadThread()).start();
		} catch (UnknownHostException e) {
			System.out.println("서버에 연결할 수 없습니다");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void SendObject(Object obj) {
		try {
			Client.oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ReadThread implements Runnable{
		
		@Override
		public void run() {
			Util util = new Util();
			MessageObject objectMsg = null;
			while(true) {
				try {
					objectMsg = (MessageObject) Client.ois.readObject();
					
					if(objectMsg.type == MessageTypeConstantNumbers.CHECK_MSG) {
						if(((CheckMessage) objectMsg).check) {
							util.alert("안내", "성공", ((CheckMessage) objectMsg).msg);
						} else {
							util.alert("안내", "실패", ((CheckMessage) objectMsg).msg);
							break;
						}
					}
				} catch (ClassNotFoundException e) {
					
				} catch (IOException e) {
					
				}
				System.out.println(11);
				util.sleep(100);
			}
		}
	}
}
