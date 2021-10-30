package db.query;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import db.JDBC;
import format.PriceInfo;
import format.message.History;

public class HistoryQuery {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void CoinHistoryUpdate(String coinName, int price) {
		updateCoinLastPrice(coinName, price);
		try {
			String sql = "SELECT coin_id, close_or_mp, high, low, time FROM `history_minute` "
					+ "WHERE coin_id = ? ORDER BY time DESC LIMIT 1";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			ResultSet rs = pstmt.executeQuery();

			Date currentTime = getCurrentTime();
			boolean isRun = false;
			while (rs.next()) {
				isRun = true;
				if (format.format(currentTime).equals(rs.getString("time"))) {
					updateMarketPrice("history_minute", coinName, price, rs.getInt("high"), rs.getInt("low"));
				} else {
					System.out.println(format.format(currentTime).equals(rs.getString("time")));
					insertMarketPrice("history_minute", coinName, price, currentTime);
				}
			}

			if (!isRun) {
				insertMarketPrice("history_minute", coinName, price, currentTime);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateCoinLastPrice(String coinName, int price) {
		try {
			String sql = "UPDATE `coin_type` SET `last_price`=? WHERE id=?";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setInt(1, price);
			pstmt.setString(2, coinName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateMarketPrice(String tableName, String coinName, int price, int high, int low) {
		try {
			PreparedStatement pstmt = null;
			if (high < price) {
				String sql = "UPDATE " + tableName
						+ " SET close_or_mp=?, high=? WHERE coin_id = ? ORDER BY time DESC LIMIT 1";
				pstmt = JDBC.con.prepareStatement(sql);
				pstmt.setInt(1, price);
				pstmt.setInt(2, price);
				pstmt.setString(3, coinName);
			} else if (low > price) {
				String sql = "UPDATE " + tableName
						+ " SET close_or_mp=?, low=? WHERE coin_id = ? ORDER BY time DESC LIMIT 1";
				pstmt = JDBC.con.prepareStatement(sql);
				pstmt.setInt(1, price);
				pstmt.setInt(2, price);
				pstmt.setString(3, coinName);
			} else {
				String sql = "UPDATE " + tableName + " SET close_or_mp=? WHERE coin_id = ? ORDER BY time DESC LIMIT 1";
				pstmt = JDBC.con.prepareStatement(sql);
				pstmt.setInt(1, price);
				pstmt.setString(2, coinName);
			}

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertMarketPrice(String tableName, String coinName, int price, Date time) {
		try {
			String sql = "INSERT INTO " + tableName + " (`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) "
					+ "VALUES (?,?,?,?,?,?);";
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			pstmt.setInt(2, price);
			pstmt.setInt(3, price);
			pstmt.setInt(4, price);
			pstmt.setInt(5, price);
			pstmt.setString(6, format.format(time));
			
			pstmt.executeUpdate();
			
			//DELETE 문에서 서브쿼리 사용이 안되므로 임시 테이블 1개를 만들어 1번 더 감쌈
			
			sql = "DELETE FROM "+ tableName + " WHERE coin_id=? "
					+ "AND time = IF((SELECT count FROM "
					+ "(SELECT count(coin_id) as count FROM " + tableName + " WHERE coin_id=?) as a) > 300, (SELECT time FROM (SELECT time FROM " + tableName + " WHERE coin_id=? ORDER BY time LIMIT 1) as b), NULL)";
			pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			pstmt.setString(2, coinName);
			pstmt.setString(3, coinName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Date getCurrentTime() {
		return new Date( (long) (Math.floor(System.currentTimeMillis()/60000)*60000) );
	}

	public ArrayList<PriceInfo> getHistory(String tableName, String coinName) {
		ArrayList<PriceInfo> infoList = new ArrayList<PriceInfo>();
		try {
			String sql = "SELECT start, close_or_mp, high, low, time FROM " + tableName
					+ " WHERE coin_id = ? ORDER BY time DESC";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			ResultSet rs = pstmt.executeQuery();

			for(byte i = 0; rs.next(); i++) {
				infoList.add(new PriceInfo(rs.getInt("start"), rs.getInt("close_or_mp"), rs.getInt("high"), rs.getInt("low"), rs.getTime("time")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return infoList;
	}
}
