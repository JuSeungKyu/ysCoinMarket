package server;

import java.util.ArrayList;
import java.util.Random;

import db.query.HistoryQuery;
import format.PriceInfo;
import format.message.History;
import util.Util;

public class InfomationSendThread implements Runnable{
	HistoryQuery q1;
	Util util;
	
	@Override
	public void run() {
		Random r = new Random();
		int randInt2 = r.nextInt(1000) - 500;
		q1 = new HistoryQuery();
		util = new Util();
		
		while (true) {
			// ----- 테스트용-----
			int randInt = r.nextInt(200) + 8000+randInt2;
			randInt2 += r.nextInt(1000) - 500;
			q1.CoinHistoryUpdate("양디코인", randInt);
			// ----- 테스트용-----

			//---- 가격 역사 보내기 ----
			for (int i = 0; i < Server.coinTypelist.size(); i++) {
				sendHistory(Server.coinTypelist.get(i));
			}
			//---- 가격 역사 보내기 ----

			util.sleep(50);
		}
	}
	
	public static void sendHistoryForOneUser(String coinId, ClientManager client) {
		
	}

	private void sendHistory(String coinId) {
		ArrayList<PriceInfo> minuteHistory = q1.getHistory("history_minute", coinId);
		
		for (int i = 0; i < Server.clientIdList.size(); i++) {
			try {
				ClientManager c = Server.clientMap.get(Server.clientIdList.get(i));
				
				if(minuteHistory.size() < c.getGraphRangeEnd()) {
					c.setGraphRangeEnd((short) minuteHistory.size());
					c.checkGraphRange();
				}
				
				if(c.getCoinType().equals(coinId)) {
					if(c.getHistoryBlockType().equals("minute")) {
						c.sendObject(new History(splitPriceInfoArrayList(minuteHistory, c.getGraphRangeStart(), c.getGraphRangeEnd())));
					} else if(c.getHistoryBlockType().equals("hour")) {
						c.sendObject(minuteHistory);
					} else if(c.getHistoryBlockType().equals("date")) {
						c.sendObject(minuteHistory);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private PriceInfo[] splitPriceInfoArrayList(ArrayList<PriceInfo> arr, short start, short end) {
		PriceInfo[] output = new PriceInfo[end-start];
		for(short i = 0; i < end - start; i++) {
			output[i] = arr.get(i+start);
		}
		
		return output;
	}
}
