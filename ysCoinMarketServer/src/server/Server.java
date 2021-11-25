package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import db.JDBC;
import db.query.UtilQuery;

public class Server {
	public static HashMap<String, ClientManager> clientMap;
	public static HashMap<String, Integer> feeMap; 
	public static ArrayList<String> clientIdList;
	public static ArrayList<String> coinTypelist;

	public static void main(String[] args) throws IOException {
		new JDBC("112.153.122.10", "skills09", "skills09", "0702");

		clientMap = new HashMap<String, ClientManager>();
		clientIdList = new ArrayList<String>();
		coinTypelist = new ArrayList<String>();
		ServerSocket serverSocket = new ServerSocket(2657);
		UtilQuery utilQuery = new UtilQuery();

		try {
			ResultSet rs = utilQuery.justGetResultSet("SELECT id, fee FROM `coin_type`");
			while (rs.next()) {
				coinTypelist.add(rs.getString("id"));
				feeMap.put(rs.getString("id"), rs.getInt("fee"));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("종목 불러오기 실패 " + e.toString());
		}
		System.out.println("종목 : " + coinTypelist.toString());

		Thread sendMessageThread = new Thread(new SendMessageThread());
		Thread userLoginManager = new Thread(new UserLoginManager(serverSocket, utilQuery));
		Thread infomationSendThread = new Thread(new InfomationSendThread(utilQuery));
		Thread historyManager = new Thread(new HistoryManager());

		sendMessageThread.start();
		userLoginManager.start();
		infomationSendThread.start();
		historyManager.start();
		System.out.println("Server Start");
	}
}
