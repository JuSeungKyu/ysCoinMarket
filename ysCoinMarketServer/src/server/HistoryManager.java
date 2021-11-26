package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import db.JDBC;
import db.query.HistoryQuery;
import db.query.UtilQuery;
import util.Util;

public class HistoryManager extends Thread {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		super.run();
		Util util = new Util();
		HistoryQuery hq = new HistoryQuery();

		while (true) {
			try {
				for (byte i = 0; i < Server.coinTypelist.size(); i++) {
					String coinId = Server.coinTypelist.get(i);

					String sql = "SELECT close_or_mp, time FROM `history_minute` "
							+ "WHERE coin_id = ? ORDER BY time DESC LIMIT 1";

					PreparedStatement pstmt = JDBC.con.prepareStatement(sql);

					pstmt.setString(1, coinId);
					ResultSet rs = pstmt.executeQuery();
					
					Date currentTime = hq.getCurrentTime();
					while (rs.next()) {
						if (!format.format(currentTime).equals(rs.getString("time"))) {
							hq.insertMarketPrice("history_minute", coinId, rs.getInt("close_or_mp"), currentTime);
						}
					}
				}
			} catch (SQLException e) {
				System.out.println("가격역사 insert를 실패하였습니다");
			}
			
			if (!util.sleep(10000)) {
				return;
			}
		}
	}
}
