package server;

import java.util.ArrayList;
import java.util.Random;

import db.query.HistoryQuery;
import db.query.OrderBookQuery;
import db.query.UtilQuery;
import format.PriceInfo;
import format.TypeInfo;
import format.message.History;
import format.message.TypeInfoUpdate;
import format.message.UpdateOrderBook;
import util.Util;

public class InfomationSendThread implements Runnable {
	HistoryQuery hq;
	Util util;
	UtilQuery utilQuery;
	OrderBookQuery obq;
	

	public InfomationSendThread(UtilQuery utilQuery) {
		this.utilQuery = utilQuery;
		this.hq = new HistoryQuery();
		this.obq = new OrderBookQuery();
		this.util = new Util();
	}

	@Override
	public void run() {

		while (true) { 
			// ---- 가격 역사, 호가정보 보내기 ----
			for (int i = 0; i < Server.coinTypelist.size(); i++) {
				sendHistoryAndOrderBook(Server.coinTypelist.get(i));
			}
			// ---- 가격 역사, 호가정보 보내기 ----

			// ---- 현재 코인 리스트 정보 보내기
			sendTypeInfo();
			// ---- 현재 코인 리스트 정보 보내기

			if(!util.sleep(50)) {
				return;
			}
		}
	}

	private void sendTypeInfo() {
		TypeInfo[] infoList = new TypeInfo[Server.coinTypelist.size()];
		for (int i = 0; i < Server.coinTypelist.size(); i++) {
			infoList[i] = new TypeInfo(
					Server.coinTypelist.get(i), 
					(int) utilQuery.justGetObject("SELECT `last_price` FROM `coin_type` WHERE id='" + Server.coinTypelist.get(i) + "'")
				);
		}
		
		for (int i = 0; i < Server.clientIdList.size(); i++) {
			ClientManager c = Server.clientMap.get(Server.clientIdList.get(i));
			SendMessageThread.addMessageQueue( c, new TypeInfoUpdate(infoList) );
		}
	}

	private void sendHistoryAndOrderBook(String coinId) {
		ArrayList<PriceInfo> history = hq.getHistory("history_minute", coinId);
			
		for (int i = 0; i < Server.clientIdList.size(); i++) {
			try {
				ClientManager c = Server.clientMap.get(Server.clientIdList.get(i));
				if(c.getCoinType().equals(coinId)) {
					if (history.size() < c.getGraphRangeEnd()) {
						c.setGraphRangeEnd((short) history.size());
						c.checkGraphRange();
					}

					SendMessageThread.addMessageQueue(c, new History(splitPriceInfoArrayList(history, c.getGraphRangeStart(), c.getGraphRangeEnd())));
					SendMessageThread.addMessageQueue(c, new UpdateOrderBook(this.obq.getOrderInfo(coinId)) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private PriceInfo[] splitPriceInfoArrayList(ArrayList<PriceInfo> arr, int start, int end) {
		PriceInfo[] output = new PriceInfo[end - start];
		for (int i = 0; i < end - start; i++) {
			output[i] = arr.get(i + start);
		}

		return output;
	}
}
