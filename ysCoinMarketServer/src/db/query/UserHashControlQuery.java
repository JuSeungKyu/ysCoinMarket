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
	
	public void hashOwnerTransfer(String coin_id, String userId, int price, int count, String type){
		if(type.equals("구매")) {
			type = "판매";
		} else {
			type = "구매";
		}
		
		UtilQuery uq = new UtilQuery();
		ResultSet rs = uq.justGetResultSet("SELECT user_id, count FROM order_info WHERE price="+price+" AND order_type='"+type+"' ORDER BY order_time DESC");
		try {
			while(rs.next() && count > 0) {
				if(rs.getInt(count) > count) {
					uq.justUpdate("UPDATE hash SET user_id='"+userId+"' WHERE user_id='"+rs.getString("user_id")+"' AND coin_id='"+coin_id+" LIMIT " + count);
				} else {
					uq.justUpdate("UPDATE hash SET user_id='"+userId+"' WHERE user_id='"+rs.getString("user_id")+"' AND coin_id='"+coin_id+" LIMIT " + rs.getInt(count));
				}
				
				count -= rs.getInt(count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
