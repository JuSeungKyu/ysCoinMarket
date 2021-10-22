package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
	public Query() {}
	
	public void justUpdate(String sql) {
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Object justGetObject(String sql) {
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getObject(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet justGetResultSet(String sql) {
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
