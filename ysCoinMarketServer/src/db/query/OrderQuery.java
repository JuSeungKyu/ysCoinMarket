package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import db.JDBC;
import format.MessageObject;
import format.TransactionDetailsInfo;
import format.message.TransactionDetailsMessage;

public class OrderQuery {
	public void buyAndSellRequest(String userId, String coinId, int price, int count, String type) {
		UtilQuery uq = new UtilQuery();
		int sellOrderCount = (int) uq.justGetObject("SELECT sum(count) FROM order_info WHERE order_type = '"+ (type.equals("구매") ? "판매" : "구매")
				+"' AND coin_id = '" + coinId + "'");

		UserHashControlQuery uhcq = new UserHashControlQuery();
		if(sellOrderCount == 0) {
			addRequest(userId, price, count, type, coinId);
		} else if(sellOrderCount < count){
			addRequest(userId, price, count-sellOrderCount, type, coinId);
			uhcq.hashOwnerTransfer(coinId, userId, price, sellOrderCount, type);
		} else if(sellOrderCount >= count){
			uhcq.hashOwnerTransfer(coinId, userId, price, count, type);
		}
		setTransactionDetails(uq, userId, coinId, count, sellOrderCount, price, type);
	}
	
	public void addRequest(String userId, int price, int count, String type, String coinId) {
		try {
			String sql = "INSERT `order_info`(user_id, price, count, order_type, coin_id) VALUES(?, ?, ?, ?, ?)";
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
	
	public void setTransactionDetails(UtilQuery uq, String userId, String coinId, int orderingAmount, int penaltyAmount, int price, String orderType) {
		uq.justUpdate("INSERT INTO `transaction_details`(`coin_id`, `ordering_amount`, `penalty_amount`, `price`, `order_type`, `user_id`) "+ 
				"VALUES ('"+coinId+"','"+orderingAmount+"','"+penaltyAmount+"','"+price+"','"+orderType+"')");
	}
	
	public MessageObject getTransactionDetails(String user_id) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<TransactionDetailsInfo> info = new ArrayList<TransactionDetailsInfo>();
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement("SELECT `coin_id`, `ordering_amount`, `penalty_amount`, `price`, `order_type`, `time` FROM `transaction_details` WHERE user_id = ?");
			pstmt.setString(1, user_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				info.add(new TransactionDetailsInfo(
						rs.getString("coin_id"), 
						rs.getInt("ordering_amount"), 
						rs.getInt("penalty_amount"), 
						rs.getInt("price"), 
						rs.getString("order_type"), 
						timeFormat.format(rs.getDate("time"))
					)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return new TransactionDetailsMessage(info);
	}
}
