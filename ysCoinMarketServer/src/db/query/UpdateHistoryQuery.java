package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import db.JDBC;

public class UpdateHistoryQuery {
	public void CoinHistoryUpdate(String coinName, int price) {
		updateCoinLastPrice(coinName, price);
		try {
			String sql = "SELECT coin_id, close_or_mp, high, low, time " + "FROM `history_minute` "
					+ "WHERE coin_id = ? ORDER BY time DESC LIMIT 1";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			ResultSet rs = pstmt.executeQuery();

			Time currentTime = getCurrentTime();
			boolean isRun = false;
			while (rs.next()) {
				isRun = true;
				if (currentTime.getMinutes() == rs.getTime("time").getMinutes()) {
					updateMarketPrice("history_minute", coinName, price, rs.getInt("high"), rs.getInt("low"));
				} else {
					insertMarketPrice("history_minute", coinName, price, currentTime);
				}
			}
			
			if(!isRun) {
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

	public void insertMarketPrice(String tableName, String coinName, int price, Time time) {
		try {
			System.out.println(time);
			String sql = "INSERT INTO " + tableName + " (`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) "
					+ "VALUES (?,?,?,?,?,?);";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			pstmt.setInt(2, price);
			pstmt.setInt(3, price);
			pstmt.setInt(4, price);
			pstmt.setInt(5, price);
			pstmt.setTime(6, time);
			pstmt.executeUpdate();
			
			sql = "DELETE FROM "+tableName+" WHERE coin_id=? AND time = IF((SELECT count(coin_id) as count FROM "+tableName+" WHERE coin_id = ?) > 30, (SELECT time FROM "+tableName+" WHERE coin_id=? ORDER BY time LIMIT 1), NULL)";
			pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			pstmt.setString(2, coinName);
			pstmt.setString(3, coinName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Time getCurrentTime() {
		return new Time(System.currentTimeMillis());
	}
}
