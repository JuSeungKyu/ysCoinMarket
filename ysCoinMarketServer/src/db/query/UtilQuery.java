package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBC;

public class UtilQuery {
	public UtilQuery() {}
	
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
				return rs.getObject(1);
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
	
	public boolean justInsert(String sql) {
		try {
			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
