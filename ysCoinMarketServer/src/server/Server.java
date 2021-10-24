package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import db.JDBC;
import db.query.HistoryQuery;
import db.query.UtilQuery;
import formet.message.CheckMessage;
import formet.message.History;
import formet.message.LoginRequest;
import util.Util;

public class Server {
	public static HashMap<String, Client> clientMap;
	public static ArrayList<String> clientIdList;
	public static ArrayList<String> coinTypelist;

	public static void main(String[] args) throws IOException {
		new JDBC("localhost", "yscoin", "root", "");

		clientMap = new HashMap<String, Client>();
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

		Thread Server = new Thread(new Runnable() {
			@Override
			public void run() {
				// 서버 소켓 설정
				while (true) {
					try {
						// 클라이언트 받음
						Socket clientSocket = serverSocket.accept();

						// 인, 아웃풋 스트림 준비
						ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
						ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

						// 아이디 받음
						LoginRequest LoginMsg = (LoginRequest) ois.readObject();

						// 존재하는 아이디인지 확인
						int idCount = (int) ((long) utilQuery
								.justGetObject("SELECT count(id) FROM users WHERE id = '" + LoginMsg.id
										+ "'AND pw = '" + LoginMsg.pw + "'"));

						if (LoginMsg.isLogin && idCount != 0) {
							oos.writeObject(new CheckMessage("로그인 되었습니다.", true));
						} else if (!LoginMsg.isLogin && idCount == 0) {
							utilQuery.justUpdate("INSERT INTO users(`id`, `pw`) VALUES ('" + LoginMsg.id + "','"
									+ LoginMsg.pw + "')");
							oos.writeObject(new CheckMessage("회원가입 되었습니다.", true));
						} else {
							System.out.println("로그인 불가");
							oos.writeObject(new CheckMessage("로그인이나 회원가입을 할 수 없습니다.", false));
							continue;
						}

						// 클라이언트 저장
						clientIdList.add(LoginMsg.id);
						clientMap.put(LoginMsg.id, new Client(LoginMsg.id, clientSocket, ois, oos));
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});

		Thread infomationSendThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Util util = new Util();
				Random r = new Random();
				HistoryQuery q1 = new HistoryQuery();
				while (true) {
					//----- 테스트용-----
					int randInt = r.nextInt(900) + 100;
					q1.CoinHistoryUpdate("양디코인", randInt);
					//----- 테스트용-----

//					for(int i = 0; i < coinTypelist.size(); i++) {
//						History history = q1.getHistory("history_minute", coinTypelist.get(i));
//						sendHistory(history);
//					}
					
					util.sleep(1000);
				}
			}
			
			private void sendHistory(History history) {
				for (int j = 0; j < clientIdList.size(); j++) {
					try {
						clientMap.get(clientIdList.get(j)).getOOS().writeObject(history);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		Server.start();
		infomationSendThread.start();
		System.out.println("TcpServer Start - (서버를 종료하려면 '종료' 입력)");

		while (true) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			if (bf.readLine().equals("종료")) {
				serverSocket.close();
				infomationSendThread.interrupt();
				Server.interrupt();
			}
		}
	}
}
