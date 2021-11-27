package db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import db.JDBC;
import format.PriceInfo;

public class OrderBookQuery {

	public ArrayList<int[]> getOrderInfo(String coinId) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		
		list.addAll(getSellOrder(coinId));
		list.addAll(getBuyOrder(coinId));

		return list;
	}

	private ArrayList<int[]> getSellOrder(String coinId) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		try {
			String sql = "SELECT price, SUM(count) FROM order_info WHERE coin_id=? AND order_type='판매' GROUP BY price ORDER BY price DESC LIMIT 5";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new int[] { rs.getInt(1), rs.getInt(2), 0 });
			}

			while (list.size() < 5) {
				int[] info = { 0, 0, 0 };
				list.add(0, info);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return getNullList((byte) 5);
		}

		return list;
	}
	
	private ArrayList<int[]> getBuyOrder(String coinId) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		try {
			String sql = "SELECT SUM(count), price FROM order_info WHERE coin_id=? AND order_type='구매' GROUP BY price ORDER BY price DESC LIMIT 5";

			PreparedStatement pstmt = JDBC.con.prepareStatement(sql);
			pstmt.setString(1, coinId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new int[] { 0, rs.getInt(1), rs.getInt(2) });
			}

			while (list.size() < 5) {
				int[] info = { 0, 0, 0 };
				list.add(info);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return getNullList((byte) 5);
		}

		return list;
	}
	
	private ArrayList<int[]> getNullList(byte size) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		
		for(byte i = 0; i < size; i++) {
			list.add(new int[] {0,0,0});
		}
		
		return list;
	}
}
