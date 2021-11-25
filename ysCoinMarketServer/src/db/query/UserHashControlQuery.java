package db.query;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import server.Server;

public class UserHashControlQuery {
	private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public long getUserHashCount(String userId, String coinname) {
		return ((long) new UtilQuery().justGetObject(
				"SELECT count(hash) FROM hash WHERE coin_id='" + coinname + "' AND user_id='" + userId + "'"));
	}

	public int getUserOrderedHashCount(String userId, String coinname) {
		return ((BigDecimal) new UtilQuery().justGetObject("SELECT IFNULL(sum(count), 0) FROM order_info WHERE coin_id='" + coinname
				+ "' AND user_id='" + userId + "' AND order_type='판매'")).intValue();
	}

	public void hashOwnerTransfer(String coinId, String userId, int price, int count, String type, long orderInfoId) {
		UtilQuery uq = new UtilQuery();
		ResultSet rs = null;
		if (type.equals("구매")) {
			rs = uq.justGetResultSet("SELECT order_time, user_id, count, id FROM order_info WHERE price<=" + price
					+ " AND order_type='판매' ORDER BY price DESC, order_time DESC");
		} else {
			rs = uq.justGetResultSet("SELECT order_time, user_id, count, id FROM order_info WHERE price>=" + price
					+ " AND order_type='구매' ORDER BY price DESC, order_time DESC");
		}
		
		try {
			while (rs.next() && count > 0) {
				String buyer = "";
				String seller = "";
				
				if(type.equals("구매")) {
					buyer = userId;
					seller = rs.getString("user_id");
				} else {
					buyer = rs.getString("user_id");
					seller = userId;
				}
				System.out.println(buyer + " " + seller);
				
				if (rs.getInt("count") > count) {
					uq.justUpdate("UPDATE hash SET user_id='" + buyer + "' WHERE user_id='" + seller
					+ "' AND coin_id='" + coinId + "' LIMIT " + count);
					uq.justUpdate("UPDATE order_info SET count=count-"+count+" WHERE id="+rs.getInt("id"));
					uq.justUpdate("UPDATE users SET money=money+"+(price*count)+" WHERE id='"+ seller + "'");
					uq.justUpdate("UPDATE users SET money=money-"+(price*count)+" WHERE id='"+ buyer + "'");
				} else {
					uq.justUpdate("UPDATE hash SET user_id='" + buyer + "' WHERE user_id='" + seller
							+ "' AND coin_id='" + coinId + "' LIMIT " + rs.getInt("count"));
					uq.justUpdate("DELETE FROM order_info WHERE id="+rs.getInt("id"));
					uq.justUpdate("UPDATE users SET money=money+"+(price*rs.getInt("count"))+" WHERE id='"+ seller + "'");
					uq.justUpdate("UPDATE users SET money=money-"+(price*rs.getInt("count"))+" WHERE id='"+ buyer + "'");
				}
				
				Server.clientMap.get(rs.getString("user_id")).sendUserInfo();
				
				updateTransactionDetails(uq, count, orderInfoId);
				updateTransactionDetails(uq, count, rs.getInt("id"));
				count -= rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateTransactionDetails(UtilQuery uq, int penaltyAmount, long orderInfoId) {
		uq.justUpdate("UPDATE `transaction_details` SET penalty_amount=penalty_amount+"+penaltyAmount+" WHERE order_info_id="+orderInfoId);
	}
	
	public String getPreviousHash(String coinId) {
		UtilQuery uq = new UtilQuery();
		ResultSet rs = uq.justGetResultSet("SELECT MAX(id), hash FROM `hash` WHERE coin_id='" + coinId + "'");
		
		try {
			if(rs.next()) {
				return rs.getString("hash");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean addBlock(String hash, String userId, String coinId) {
		UtilQuery uq = new UtilQuery();
		return uq.justInsert("INSERT INTO `hash`(`hash`, `user_id`, `coin_id`) VALUES ('" + hash + "''" + userId + "''" + coinId + "')");
	}
}
