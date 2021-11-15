package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import db.query.OrderQuery;
import db.query.UserHashControlQuery;
import db.query.UtilQuery;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;
import format.message.BuyRequest;
import format.message.CheckMessage;
import format.message.SellRequest;
import format.message.TransactionDetailsMessage;
import format.message.UpdateGraphRange;
import util.MessageInfo;
import format.message.CoinTypeChange;
import format.message.PreviousHashMessage;
import format.message.PreviousHashRequest;


public class ClientManager extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String id;
	private Socket socket;
	private boolean isReady; 
	
	private String coinType = "양디코인";
	private short[] graphRange = {0, 30};

	public ClientManager(String id, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.id = id;
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
		
		this.start();
	}

	@Override
	public void run() {
		super.run();
		try {
			while (true) {
				try {
					MessageObject msg = (MessageObject) ois.readObject();
					
					if (msg == null)
						break;
					
					if (msg.type == MessageTypeConstantNumbers.BUY_REQEUST) {
						buyRequest((BuyRequest)msg);
						continue;
					}
					
					if (msg.type == MessageTypeConstantNumbers.SELL_REQEUST) {
						sellRequest((SellRequest)msg);
						continue;
					}
					
					if (msg.type == MessageTypeConstantNumbers.CHAGNE_COIN_TYPE) {
						this.coinType = ((CoinTypeChange) msg).coinId;
						continue;
					}
					
					if(msg.type == MessageTypeConstantNumbers.UPDATE_GRAPH_RANGE) {
						setGraphRange(((UpdateGraphRange) msg).range);
						continue;
					}
					
					if(msg.type == MessageTypeConstantNumbers.TRANSACTION_DETAILS_REQUEST) {
						sendTransactionDetailsMessage();
						continue;
					}
					
					if(msg.type == MessageTypeConstantNumbers.PREVIOUS_HASH_REQUEST) {
						System.out.println("get Object");
						sendPreviousHash(((PreviousHashRequest)msg).coinId);
						continue;
					}
					
				} catch (ClassNotFoundException e) {
					removeClient();
				}
			}
			
			removeClient();
		} catch (IOException e) {
			removeClient();
		}
	}
	
	private void sendPreviousHash(String coinId) {
		 UserHashControlQuery uhcq = new UserHashControlQuery();
		 String hash = uhcq.getPreviousHash(coinId);
		 if(!hash.isEmpty()) {
			 System.out.println("send Object");
			 SendMessageThread.addMessageQueue(this, new PreviousHashMessage(hash));
		 }
	}
	
	private void sendTransactionDetailsMessage() {
		TransactionDetailsMessage info = new OrderQuery().getTransactionDetails(this.id);
		if(info != null) {
			SendMessageThread.addMessageQueue(this, info);
		}
	}
	
	private void setGraphRange(short[] graphRange) {
		this.graphRange[0] += graphRange[0];
		this.graphRange[1] += graphRange[1];
		
		checkGraphRange();
	}
	
	public void checkGraphRange() {
		if(this.graphRange[1] < 0) {
			this.graphRange[1] = 1;
		}
		
		if(this.graphRange[0] >= this.graphRange[1]) {
			graphRange[0] = (short) (this.graphRange[1] - 1);
		}
		
		if(this.graphRange[0] < 0) {
			this.graphRange[0] = 0;
		}
	}
	
	private void sellRequest(SellRequest msg) {
		UserHashControlQuery uhcq = new UserHashControlQuery();
		if(uhcq.getUserHashCount(this.id, msg.coinname) > msg.count - uhcq.getUserOrderedHashCount(this.id, msg.coinname)) {
			sendCheckMessage("매도 주문 실패", false);
		} else {
			new OrderQuery().buyAndSellRequest(this.id, msg.coinname, msg.price, msg.count, "판매");
			sendCheckMessage("매도 주문 성공", true);
		}
	}
	
	private void buyRequest(BuyRequest msg) {
		int money = (int) new UtilQuery().justGetObject("SELECT money FROM users WHERE id = '" + this.id + "'");
		if(msg.count * msg.price > money) {
			sendCheckMessage("매수 주문 실패", false);
		} else{
			new OrderQuery().buyAndSellRequest(this.id, msg.coinname, msg.price, msg.count, "구매");
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
		SendMessageThread.addMessageQueue(this, new CheckMessage(msg, result));
	}
	
	public void sendObject(Object object) {
		try {
			if(socket.isClosed())
				throw new Error();
			this.oos.writeObject(object);
			this.oos.flush();
		} catch (IOException e) {
			removeClient();
		}
	}
	
	public boolean isReady() {
		return this.isReady;
	}
	
	public void setCoinType(String coinId) {
		this.coinType = coinId;
	}
	
	public String getCoinType() {
		return this.coinType;
	}
	
	public short getGraphRangeStart() {
		return this.graphRange[0];
	}
	
	public short getGraphRangeEnd() {
		return this.graphRange[1];
	}

	public void setGraphRangeEnd(short size) {
		this.graphRange[1] = size;
	}
}
