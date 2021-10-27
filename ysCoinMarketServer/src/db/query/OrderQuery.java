package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBC;

public class OrderQuery {
	public void buyRequest(String userId, String coinname, int price, int count) {
		int sellOrderCount = (int) new UtilQuery().justGetObject("SELECT sum(count) FROM ordre_info WHERE order_type = '판매' AND coin_id = '" + coinname + "'");
		
		if(sellOrderCount == 0) {
			addRequest(userId, price, count, "구매", coinname);
		} else if(sellOrderCount > count){
			addRequest(userId, price, count-sellOrderCount, "구매", coinname);
		} else if(sellOrderCount == count){
			
		} else if(sellOrderCount < count){
			
		}
		
//		try {
//			String sql = "UPDATE `coin_type` SET `last_price`=? WHERE id=?";
//
//			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
//			pstmt.setInt(1, price);
//			pstmt.setString(2, coinname);
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	public void sellRequest(String userId, String coinname, int price, int conut) {
		
	}
	
	public void addRequest(String userId, int price, int count, String type, String coinId) {
		try {
			String sql = "INSERT `order_info`(user_id, price, count, order_type, coin_id) VALUE(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, price);
			pstmt.setInt(3, count);
			pstmt.setString(5, type);
			pstmt.setString(5, coinId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
