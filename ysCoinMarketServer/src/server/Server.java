package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import db.JDBC;
import db.query.UtilQuery;

public class Server {
	public static HashMap<String, ClientManager> clientMap;
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
			ResultSet coinTypeRs = utilQuery.justGetResultSet("SELECT id FROM `coin_type`");
			while (coinTypeRs.next()) {
				coinTypelist.add(coinTypeRs.getString("id"));
			}
			coinTypeRs.close();
		} catch (Exception e) {
			System.out.println("종목 불러오기 실패 " + e.toString());
		}
		System.out.println("종목 : " + coinTypelist.toString());

		Thread sendMessageThread = new Thread(new SendMessageThread());
		Thread userLoginManager = new Thread(new UserLoginManager(serverSocket, utilQuery));
		Thread infomationSendThread = new Thread(new InfomationSendThread(utilQuery));

		sendMessageThread.start();
		userLoginManager.start();
		infomationSendThread.start();
		System.out.println("Server Start - (서버를 종료하려면 '종료' 입력)");

		while (true) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			if (bf.readLine().equals("종료")) {
				serverSocket.close();
				infomationSendThread.interrupt();
				userLoginManager.interrupt();
				sendMessageThread.interrupt();
			}
		}
	}
}
