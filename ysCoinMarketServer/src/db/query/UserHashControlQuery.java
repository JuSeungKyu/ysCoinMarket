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

	public void hashOwnerTransfer(String coin_id, String userId, int price, int count, String type) {
		UtilQuery uq = new UtilQuery();
		ResultSet rs = null;
		if (type.equals("구매")) {
			type = "판매";
			rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price<=" + price
					+ " AND order_type='" + type + "' ORDER BY price order_time DESC");
		} else {
			type = "구매";
			rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price>=" + price
					+ " AND order_type='" + type + "' ORDER BY price order_time DESC");
		}
		
		try {
			while (rs.next() && count > 0) {
				if (rs.getInt(count) > count) {
					uq.justUpdate("UPDATE hash SET user_id='" + userId + "' WHERE user_id='" + rs.getString("user_id")
							+ "' AND coin_id='" + coin_id + " LIMIT " + count);
				} else {
					uq.justUpdate("UPDATE hash SET user_id='" + userId + "' WHERE user_id='" + rs.getString("user_id")
							+ "' AND coin_id='" + coin_id + " LIMIT " + rs.getInt(count));
				}

				count -= rs.getInt(count);
			}
		} catch (Exception e) {}
	}
}
