package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

import db.query.OrderQuery;
import db.query.UserHashControlQuery;
import db.query.UtilQuery;
import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.message.BuyRequest;
import format.message.CheckMessage;
import format.message.SellRequest;
import format.message.TransactionDetailsMessage;
import format.message.UpdateGraphRange;
import format.message.UserInfoMsg;
import format.message.CoinTypeChange;
import format.message.MineBlockRequest;
import format.message.PreviousHashMessage;
import format.message.PreviousHashRequest;

public class ClientManager extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String id;
	private Socket socket;
	private boolean isReady;

	private String coinType = "양디코인";
	private int[] graphRange = { 0, 30 };

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
						buyRequest((BuyRequest) msg);
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.SELL_REQEUST) {
						sellRequest((SellRequest) msg);
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.CHAGNE_COIN_TYPE) {
						this.coinType = ((CoinTypeChange) msg).coinId;
						this.sendUserInfo();
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.UPDATE_GRAPH_RANGE) {
						setGraphRange(((UpdateGraphRange) msg).range);
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.TRANSACTION_DETAILS_REQUEST) {
						sendTransactionDetailsMessage();
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.PREVIOUS_HASH_REQUEST) {
						sendPreviousHash(((PreviousHashRequest) msg).coinId);
						continue;
					}

					if (msg.type == MessageTypeConstantNumbers.BLOCK_MINE_REQUEST) {
						addNewBlock(((MineBlockRequest) msg).hash, ((MineBlockRequest) msg).coinId);
						this.sendUserInfo();
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

	private void addNewBlock(String hash, String coinId) {
		UserHashControlQuery uhcq = new UserHashControlQuery();
		uhcq.addBlock(hash, this.id, coinId);
	}

	private void sendPreviousHash(String coinId) {
		UserHashControlQuery uhcq = new UserHashControlQuery();
		String hash = uhcq.getPreviousHash(coinId);
		if (!hash.isEmpty()) {
			SendMessageThread.addMessageQueue(this, new PreviousHashMessage(hash));
		}
	}

	private void sendTransactionDetailsMessage() {
		TransactionDetailsMessage info = new OrderQuery().getTransactionDetails(this.id);
		if (info != null) {
			SendMessageThread.addMessageQueue(this, info);
		}
	}

	private void setGraphRange(short[] graphRange) {
		if(this.graphRange[1] - this.graphRange[0] < 5) {
			this.graphRange[0] -= 3;
		}
		
		if (this.graphRange[0] + graphRange[0] > Short.MAX_VALUE) {
			return;
		}

		if (this.graphRange[1] + graphRange[1] > Short.MAX_VALUE) {
			return;
		}
		
		short range = (short) ((this.graphRange[1] + graphRange[1]) - (this.graphRange[0] + graphRange[0]));

		if (range > 100 || range < 5) {
			return;
		}
		
		this.graphRange[0] += graphRange[0];
		this.graphRange[1] += graphRange[1];
		

		checkGraphRange();
	}

	public void checkGraphRange() {

		if (this.graphRange[1] < 0) {
			this.graphRange[1] = 1;
		}

		if (this.graphRange[0] >= this.graphRange[1]) {
			graphRange[0] = (short) (this.graphRange[1] - 1);
		}

		if (this.graphRange[0] < 0) {
			this.graphRange[0] = 0;
		}
	}

	private void sellRequest(SellRequest msg) {
		if (msg.count < 1) {
			sendCheckMessage("주문량이 1보다 작습니다", false);
			return;
		}

		UserHashControlQuery uhcq = new UserHashControlQuery();
		if (msg.count > uhcq.getUserHashCount(this.id, msg.coinname)
				- uhcq.getUserOrderedHashCount(this.id, msg.coinname)) {
			sendCheckMessage("매도 주문 실패", false);
		} else {
			new OrderQuery().buyAndSellRequest(this.id, msg.coinname, msg.price, msg.count, "판매");
			sendCheckMessage("매도 주문 성공", true);
		}
		this.sendUserInfo();
	}

	private void buyRequest(BuyRequest msg) {
		if (msg.count < 1) {
			sendCheckMessage("주문량이 1보다 작습니다", false);
			return;
		}

		long price = msg.count * msg.price;

		if (price > getMoney()) {
			sendCheckMessage("돈이 부족합니다.", false);
		} else {
			new OrderQuery().buyAndSellRequest(this.id, msg.coinname, msg.price, msg.count, "구매");
			sendCheckMessage("매수 주문 성공", true);
		}
		this.sendUserInfo();
	}

	public void removeClient() {
		Server.clientMap.remove(id);
		for (int i = 0; i < Server.clientIdList.size(); i++) {
			if (Server.clientIdList.get(i).equals(this.id)) {
				Server.clientIdList.remove(i);
				break;
			}
		}
		try {
			this.socket.close();
			System.out.println(this.id + "님이 로그아웃 하셨습니다.");
		} catch (IOException e) {
			System.out.println(this.id + "님의 소켓이 이미 닫혀있습니다");
		}
	}

	public void sendCheckMessage(String msg, boolean result) {
		SendMessageThread.addMessageQueue(this, new CheckMessage(msg, result));
	}

	public void sendObject(Object object) {
		try {
			if (socket.isClosed())
				removeClient();
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

	public int getGraphRangeStart() {
		return this.graphRange[0];
	}

	public int getGraphRangeEnd() {
		return this.graphRange[1];
	}

	public void setGraphRangeStart(short size) {
		this.graphRange[1] = size;
	}
	
	public void setGraphRangeEnd(short size) {
		this.graphRange[1] = size;
	}

	public void sendUserInfo() {
		UserHashControlQuery uhcq = new UserHashControlQuery();
		SendMessageThread.addMessageQueue(this, new UserInfoMsg(
				this.getMoney(),
				uhcq.getUserHashCount(this.id, this.coinType) - uhcq.getUserOrderedHashCount(this.id, this.coinType),
				Server.feeMap.get(this.coinType)
			));
	}

	private long getMoney() {
		UtilQuery uq = new UtilQuery();
		Object money1 = uq.justGetObject("SELECT money FROM users WHERE id = '" + this.id + "'");
		Object money2 = uq.justGetObject("SELECT SUM(price * count) as price, user_id FROM `order_info` WHERE user_id='"
				+ this.id + "' AND order_type='구매'");

		if (money1 == null) {
			return 0;
		}

		if (money2 == null) {
			return (long) money1;
		}

		return (long)money1 - ((BigDecimal) money2).longValue();
	}
}
