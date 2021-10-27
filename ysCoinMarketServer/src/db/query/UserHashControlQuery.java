package db.query;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserHashControlQuery {
	public int getUserHashCount(String userId, String coinname) {
		return 0;
	}
	
	public int getUserOrderedHashCount(String userid, String coinname) {
		return 0;
	}
	
	public void hashOwnerTransfer(String coinId, String userId, int price, int count, String type){
		ResultSet rs = new UtilQuery().justGetResultSet("SELECT user_id, count FROM order_info WHERE price="+price+" AND order_type='"+type+"' ORDER BY order_time DESC");
		try {
			while(rs.next() && count > 0) {
				if(rs.getInt("count") <= count) {
					count -= rs.getInt("count");
					new UtilQuery().justUpdate("UPDATE hash SET user_id='"+userId+"' WHERE coin_id='"+coinId+"' AND user_id = '"+rs.getString("user_id")+"'");
				} else {
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
