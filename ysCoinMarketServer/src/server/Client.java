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
<<<<<<< Updated upstream
import formet.MessageObject;
import formet.message.BuyRequest;
import formet.message.CheckMessage;
import formet.message.SellRequest;
import formet.MessageTypeConstantNumbers;
=======
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.message.BuyRequest;
import format.message.CheckMessage;
import format.message.CoinTypeChange;
import format.message.SellRequest;
>>>>>>> Stashed changes

public class Client extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String id;
	private Socket socket;
	private boolean isReady; 
<<<<<<< Updated upstream
=======

	private String coinType = "양디코인";
	private String historyBlockType = "minute";
>>>>>>> Stashed changes

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
					
					System.out.println(msg.type);
					
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
					
					if (msg.type == MessageTypeConstantNumbers.CHAGNE_COIN_TYPE) {
						System.out.println(11);
						this.coinType = ((CoinTypeChange) msg).coinId;
						this.historyBlockType = ((CoinTypeChange) msg).historyBlock;
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
<<<<<<< Updated upstream
=======
	
	public String getCoinType() {
		return this.coinType;
	}
	
	public String getHistoryBlockType() {
		return this.historyBlockType;
	}
>>>>>>> Stashed changes
}
