package db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.JDBC;


public class UserHashControlQuery {
	public int getUserHashCount(String userId, String coinname) {
		JDBC db = new JDBC("localhost", "yscoin", "root", "");
	
		Connection con = (Connection) db;
		PreparedStatement pstmt = null;
		String sql ="SELECT COUNT(hash) FROM `hash` WHERE user_id = ? AND coin_id = ?";
		
		try {
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userId);
			pstmt.setString(2, coinname);
		} catch (Exception e) {
			
		}
		
		return 0;
	}

	public int getUserOrderedHashCount(String userid, String coinname) {

		return 0;
	}

	public void hashOwnerTransfer(String coin_id, String userId, int price, int count, String type) {
		if (type.equals("구매")) {
			type = "판매";
		} else {
			type = "구매";
		}

		UtilQuery uq = new UtilQuery();
		ResultSet rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price=" + price
				+ " AND order_type='" + type + "' ORDER BY order_time DESC");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
