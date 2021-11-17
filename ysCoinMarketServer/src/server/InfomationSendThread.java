package server;

import java.util.ArrayList;
import java.util.Random;

import db.query.HistoryQuery;
import db.query.UtilQuery;
import format.PriceInfo;
import format.TypeInfo;
import format.message.History;
import format.message.TypeInfoUpdate;
import util.Util;

public class InfomationSendThread implements Runnable {
	HistoryQuery q1;
	Util util;
	UtilQuery utilQuery;

	public InfomationSendThread(UtilQuery utilQuery) {
		this.utilQuery = utilQuery;
	}

	@Override
	public void run() {
		Random r = new Random();
		int randInt2 = r.nextInt(1000) - 500;
		q1 = new HistoryQuery();
		util = new Util();

		while (true) {
			// ----- 테스트용-----
			int randInt = r.nextInt(200) + 8000 + randInt2;
			randInt2 += r.nextInt(1000) - 500;
			q1.CoinHistoryUpdate("양디코인", randInt);
			q1.CoinHistoryUpdate("바이트코인", randInt);
			// ----- 테스트용-----

			// ---- 가격 역사 보내기 ----
			for (int i = 0; i < Server.coinTypelist.size(); i++) {
				sendHistory(Server.coinTypelist.get(i));
			}
			// ---- 가격 역사 보내기 ----

			// ---- 현재 코인 리스트 정보 보내기
			sendTypeInfo();
			// ---- 현재 코인 리스트 정보 보내기

			util.sleep(50);
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
			SendMessageThread.addMessageQueue( Server.clientMap.get(Server.clientIdList.get(i)), new TypeInfoUpdate(infoList) );
		}
	}

	private void sendHistory(String coinId) {
		ArrayList<PriceInfo> history = q1.getHistory("history_minute", coinId);

		for (int i = 0; i < Server.clientIdList.size(); i++) {
			try {
				ClientManager c = Server.clientMap.get(Server.clientIdList.get(i));
				if(c.getCoinType().equals(coinId)) {
					if (history.size() < c.getGraphRangeEnd()) {
						c.setGraphRangeEnd((short) history.size());
						c.checkGraphRange();
					}

					SendMessageThread.addMessageQueue(c, new History(splitPriceInfoArrayList(history, c.getGraphRangeStart(), c.getGraphRangeEnd())));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private PriceInfo[] splitPriceInfoArrayList(ArrayList<PriceInfo> arr, short start, short end) {
		PriceInfo[] output = new PriceInfo[end - start];
		for (short i = 0; i < end - start; i++) {
			output[i] = arr.get(i + start);
		}

		return output;
	}
}
