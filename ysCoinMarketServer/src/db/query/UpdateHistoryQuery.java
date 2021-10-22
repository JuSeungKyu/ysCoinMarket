package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import db.JDBC;

public class UpdateHistoryQuery {
	public void CoinHistoryUpdate(String coinName, int price) {
		updateCoinLastPrice(coinName, price);
		
		try {
			String sql = "SELECT coin_id, close_or_mp, high, low, time "
					+ "FROM `history_second` "
					+ "WHERE coin_id = ? ORDER BY time DESC LIMIT 1";
			
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			ResultSet rs = pstmt.executeQuery();
			
			Time currentTime = getCurrentSecond();
			while (rs.next()) {
				if(currentTime.equals(rs.getTime("time"))) {
					
				} else {
					insertMarketPrice("history_second", coinName, price, currentTime);
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		updateMarketPrice("history_second", coinName, price);
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
	
	public void updateMarketPrice(String tableName, String coinName, int price) {
		try {
			String sql = "UPDATE "+tableName+" SET close_or_mp=? WHERE coin_id = ? ORDER BY time DESC LIMIT 1";
			
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setInt(1, price);
			pstmt.setString(2, coinName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertMarketPrice(String tableName, String coinName, int price, Time time) {
		try {
			String sql = "INSERT INTO "+tableName+"(`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) "
					+ "VALUES (?,?,?,?,?,?)";
			
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinName);
			pstmt.setInt(2, price);
			pstmt.setInt(3, price);
			pstmt.setInt(4, price);
			pstmt.setInt(5, price);
			pstmt.setTime(6, time);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Time getCurrentSecond() {
        return new Time(System.currentTimeMillis());
	}
}
