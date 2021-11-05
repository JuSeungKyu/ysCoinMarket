package db.query;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UserHashControlQuery {
	private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public int getUserHashCount(String userId, String coinname) {
		return (int) new UtilQuery().justGetObject(
				"SELECT count(hash) FROM hash WHERE coin_id='" + coinname + "' AND user_id='" + userId + "'");
	}

	public int getUserOrderedHashCount(String userId, String coinname) {
		return (int) new UtilQuery().justGetObject("SELECT sum(count) FROM order_info WHERE coin_id='" + coinname
				+ "' AND user_id='" + userId + "' AND order_type='구매'");
	}

	public void hashOwnerTransfer(String coinId, String userId, int price, int count, String type) {
		UtilQuery uq = new UtilQuery();
		ResultSet rs = null;
		if (type.equals("구매")) {
			rs = uq.justGetResultSet("SELECT order_time, user_id, count FROM order_info WHERE price<=" + price
					+ " AND order_type='판매' ORDER BY price DESC, order_time DESC");
		} else {
			rs = uq.justGetResultSet("SELECT order_time, user_id, count FROM order_info WHERE price>=" + price
					+ " AND order_type='구매' ORDER BY price DESC, order_time DESC");
		}
		
		try {
			while (rs.next() && count > 0) {
				String buyer = "";
				String seller = "";
				String time = timeFormat.format(rs.getString("order_time"));
				
				if(type.equals("구매")) {
					buyer = userId;
					seller = rs.getString("user_id");
				} else {
					buyer = rs.getString("user_id");
					seller = userId;
				}
				
				if (rs.getInt(count) > count) {
					uq.justUpdate("UPDATE hash SET user_id='" + buyer + "' WHERE user_id='" + seller
					+ "' AND coin_id='" + coinId + " LIMIT " + count);
					uq.justUpdate("UPDATE order_info SET count=count-"+count+" WHERE user_id='" + rs.getString("user_id") +"' AND price="+price + " AND coin_id='"+coinId+"' AND order_time='"+time+"'");
					uq.justUpdate("UPDATE users SET money=money+"+(price*count)+" WHERE id='"+ seller + "'");
					updateTransactionDetails(uq, userId, coinId, count, time);
				} else {
					uq.justUpdate("UPDATE hash SET user_id='" + buyer + "' WHERE user_id='" + seller
							+ "' AND coin_id='" + coinId + " LIMIT " + rs.getInt(count));
					uq.justUpdate("DELETE FROM order_info WHERE user_id='" + rs.getString("user_id") +"' price="+price + " coin_id='"+coinId+"' AND order_time='"+time+"'");
					uq.justUpdate("UPDATE users SET money=money+"+(price*rs.getInt(count))+" WHERE id='"+ seller + "'");
					updateTransactionDetails(uq, userId, coinId, rs.getInt(count), time);
				}

				count -= rs.getInt(count);
			}
		} catch (Exception e) {}
		
	}
	
	public void updateTransactionDetails(UtilQuery uq, String userId, String coinId, int penaltyAmount, String time) {
		uq.justUpdate("UPDATE `transaction_details` SET penalty_amount=penalty_amount+"+penaltyAmount+" WHERE coin_id='"+coinId+"' AND user_id='"+userId+"' AND time='"+time+"'");
	}
}
