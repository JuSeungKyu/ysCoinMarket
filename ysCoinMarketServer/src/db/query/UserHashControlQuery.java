package db.query;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.xdevapi.Result;

public class UserHashControlQuery {
	public int getUserHashCount(String userId, String coinname) {
		return 0;
	}
	
	public int getUserOrderedHashCount(String userid, String coinname) {
		return 0;
	}
	
	public void hashOwnerTransfer(String userId, int price, int count, String type){
		ResultSet rs = new UtilQuery().justGetResultSet("SELECT user_id, count FROM order_info WHERE price="+price+" AND order_type='"+type+"' ORDER BY order_time DESC");
		try {
			while(rs.next() && count > 0) {
				rs.getString("user_id");
				rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
