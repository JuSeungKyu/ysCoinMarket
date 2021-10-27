package server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.ws.handler.MessageContext;

import db.query.OrderQuery;
import db.query.UserHashControlQuery;
import db.query.UtilQuery;
import formet.MessageObject;
import formet.message.BuyRequest;
import formet.message.CheckMessage;
import formet.message.SellRequest;
import formet.MessageTypeConstantNumbers;

public class Client extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String id;
	private Socket socket;
	private boolean isReady; 

	public Client(String id, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.id = id;
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	@Override
	public void run() {
		super.run();
		try {
			while (true) {
				try {
					MessageObject msg = (MessageObject) ois.readObject();

					if (msg == null) {
						break;
					}
					
					if (msg.type == MessageTypeConstantNumbers.BUY_REQEUST) {
						buyRequest((BuyRequest)msg);
						continue;
					}
					
					if (msg.type == MessageTypeConstantNumbers.SELL_REQEUST) {
						sellRequest((SellRequest)msg);
						continue;
					}
					
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			removeClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sellRequest(SellRequest msg) {
		UserHashControlQuery uhcq = new UserHashControlQuery();
		if(uhcq.getUserHashCount(this.id, msg.coinname) > msg.count - uhcq.getUserOrderedHashCount(this.id, msg.coinname)) {
			sendCheckMessage("매도 주문 실패", false);
		} else {
			new OrderQuery().sellRequest(this.id, msg.coinname, msg.price, msg.count);
			sendCheckMessage("매도 주문 성공", true);
		}
	}
	
	public void buyRequest(BuyRequest msg) {
		int money = (int) new UtilQuery().justGetObject("SELECT money FROM users WHERE id = '" + this.id + "'");
		if(msg.count * msg.price > money) {
			sendCheckMessage("매수 주문 실패", false);
		} else{
			new OrderQuery().buyRequest(this.id, msg.coinname, msg.price, msg.count);
			sendCheckMessage("매수 주문 성공", true);
		}
	}
	
	public void removeClient() {
		Server.clientMap.remove(id);
		for(int i = 0; i < Server.clientIdList.size(); i++) {
			if(Server.clientIdList.get(i).equals(this.id)) {
				Server.clientIdList.remove(i);
				break;
			}
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCheckMessage(String msg, boolean result) {
		sendObject(new CheckMessage(msg, result));
	}
	
	public void sendObject(Object object) {
		try {
			this.oos.writeObject(object);
			this.oos.flush();
		} catch (IOException e) {
			removeClient();
		}
	}
	
	public boolean isReady() {
		return this.isReady;
	}
}
