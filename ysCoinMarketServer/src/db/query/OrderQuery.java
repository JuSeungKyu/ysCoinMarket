package db.query;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.JDBC;
import format.TransactionDetailsInfo;
import format.message.TransactionDetailsMessage;

public class OrderQuery {
	public void buyAndSellRequest(String userId, String coinId, int price, int count, String type) {
		UtilQuery uq = new UtilQuery();
		int orderCount = ((BigDecimal) uq.justGetObject("SELECT IFNULL(sum(count), 0) FROM order_info WHERE order_type = '"+ (type.equals("구매") ? "판매" : "구매")
				+"' AND coin_id = '" + coinId + "' AND price" + (type.equals("구매") ? "<" : ">") + "=" + price)).intValue();
		
		long orderInfoId = (long) uq.justGetObject("SELECT IFNULL(MAX(id), 0) FROM `order_info`");
		
		UserHashControlQuery uhcq = new UserHashControlQuery();
		if(orderCount == 0) {
			addRequest(userId, price, count, type, coinId);
		} else if(orderCount < count){
			uhcq.hashOwnerTransfer(coinId, userId, price, orderCount, type, orderInfoId);
			addRequest(userId, price, count-orderCount, type, coinId);
			new HistoryQuery().CoinHistoryUpdate(coinId, price);
		} else if(orderCount >= count){
			uhcq.hashOwnerTransfer(coinId, userId, price, count, type, orderInfoId);
			new HistoryQuery().CoinHistoryUpdate(coinId, price);
		}
		
		setTransactionDetails(uq, userId, coinId, count, Math.min(count, orderCount), price, type, orderInfoId);
	}
	
	public void addRequest(String userId, int price, int count, String type, String coinId) {
		try {
			String sql = "INSERT `order_info`(user_id, price, count, order_type, coin_id) VALUES(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, price);
			pstmt.setInt(3, count);
			pstmt.setString(4, type);
			pstmt.setString(5, coinId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setTransactionDetails(UtilQuery uq, String userId, String coinId, int orderingAmount, int penaltyAmount, int price, String orderType, long orderInfoId) {
		uq.justUpdate("INSERT INTO `transaction_details`(`coin_id`, `ordering_amount`, `penalty_amount`, `price`, `order_type`, `user_id`, `order_info_id`) "+ 
				"VALUES ('"+coinId+"','"+orderingAmount+"','"+penaltyAmount+"','"+price+"','"+orderType+"','"+userId+"','"+orderInfoId+"')");
	}
	
	public TransactionDetailsMessage getTransactionDetails(String user_id) {
		ArrayList<TransactionDetailsInfo> info = new ArrayList<TransactionDetailsInfo>();
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement("SELECT `coin_id`, `ordering_amount`, `penalty_amount`, `price`, `order_type`, `time` FROM `transaction_details` WHERE user_id = ? ORDER BY time DESC");
			pstmt.setString(1, user_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				info.add(new TransactionDetailsInfo(
						rs.getString("coin_id"), 
						rs.getInt("ordering_amount"), 
						rs.getInt("penalty_amount"), 
						rs.getInt("price"), 
						rs.getString("order_type"), 
						rs.getString("time")
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
