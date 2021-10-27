package db.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.JDBC;

public class OrderQuery {
	public void buyAndRequest(String userId, String coinname, int price, int count, String type) {
		int sellOrderCount = (int) new UtilQuery().justGetObject("SELECT sum(count) FROM ordre_info WHERE order_type = '"+ (type.equals("구매") ? "판매" : "구매")
				+"' AND coin_id = '" + coinname + "'");

		UserHashControlQuery uhcq = new UserHashControlQuery();
		if(sellOrderCount == 0) {
			addRequest(userId, price, count, type, coinname);
		} else if(sellOrderCount < count){
			addRequest(userId, price, count-sellOrderCount, type, coinname);
			uhcq.hashOwnerTransfer(coinname, userId, price, sellOrderCount, type);
		} else if(sellOrderCount >= count){
			uhcq.hashOwnerTransfer(coinname, userId, price, count, type);
		}
	}
	
	public void addRequest(String userId, int price, int count, String type, String coinId) {
		try {
			String sql = "INSERT `order_info`(user_id, price, count, order_type, coin_id) VALwUE(?, ?, ?, ?, ?)";
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
