package db.query;

import java.sql.ResultSet;

public class UserHashControlQuery {
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
			rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price<=" + price
					+ " AND order_type='판매' ORDER BY price DESC, order_time DESC");
		} else {
			rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price>=" + price
					+ " AND order_type='구매' ORDER BY price DESC, order_time DESC");
		}
		
		try {
			
			int beforeCount = count;
			
			while (rs.next() && count > 0) {
				String Buyer = "";
				String Seller = "";
				
				if(type.equals("구매")) {
					Buyer = userId;
					Seller = rs.getString("user_id");
				} else {
					Buyer = rs.getString("user_id");
					Seller = userId;
				}
				
				if (rs.getInt(count) > count) {
					uq.justUpdate("UPDATE hash SET user_id='" + Buyer + "' WHERE user_id='" + Seller
					+ "' AND coin_id='" + coinId + " LIMIT " + count);
					uq.justUpdate("UPDATE order_info SET count=count-"+count+" WHERE user_id='" + rs.getString("user_id") +"' price="+price + " coin_id='"+coinId+"' ORDER BY order_time LIMIT 1");
					uq.justUpdate("UPDATE users SET money=money+"+(price*count)+" WHERE id='"+ Seller + "'");
				} else {
					uq.justUpdate("UPDATE hash SET user_id='" + Buyer + "' WHERE user_id='" + Seller
							+ "' AND coin_id='" + coinId + " LIMIT " + rs.getInt(count));
					uq.justUpdate("DELETE FROM order_info WHERE user_id='" + rs.getString("user_id") +"' price="+price + " coin_id='"+coinId+"' ORDER BY order_time LIMIT 1");
					uq.justUpdate("UPDATE users SET money=money+"+(price*rs.getInt(count))+" WHERE id='"+ Seller + "'");
				}

				count -= rs.getInt(count);
			}
		} catch (Exception e) {}
		
	}
	
	public void updateTransactionDetails(String userId, String coinId, int count, int penaltyAmount) {
		
	}
}
