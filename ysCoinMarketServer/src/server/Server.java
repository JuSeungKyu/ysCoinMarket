package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import db.JDBC;
import db.query.OrderQuery;
import db.query.UpdateHistoryQuery;
import db.query.UtilQuery;
import formet.message.CheckMessage;
import formet.message.LoginRequest;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

public class Server {
	public static HashMap<String, Client> clientMap;

	public static void main(String[] args) throws IOException {
		new JDBC("localhost", "yscoin", "root", "");
		
		clientMap = new HashMap<String, Client>();
		ServerSocket serverSocket = new ServerSocket(2657);
		
		Thread server = new Thread(new Runnable() {
			@Override
			public void run() {
				UtilQuery query = new UtilQuery();
				UpdateHistoryQuery q1 = new UpdateHistoryQuery();
				q1.CoinHistoryUpdate("양디코인", 300);
				q1.CoinHistoryUpdate("양디코인", 500);
				q1.CoinHistoryUpdate("양디코인", 100);
				q1.CoinHistoryUpdate("양디코인", 400);
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
						int idCount = (int)query.justGetObject("SELECT count(users_id) FROM users WHERE users_id = '" + LoginMsg.id +  "' users_pw = '" + LoginMsg.pw +"'");
						
						if(LoginMsg.isLogin && idCount != 0) {
							oos.writeObject(new CheckMessage("로그인 되었습니다.", true));
						} else if(!LoginMsg.isLogin && idCount == 0) {
							query.justUpdate("INSERT INTO users(`id`, `pw`) VALUES ('"+LoginMsg.id+ "','"+LoginMsg.pw+"')");
							oos.writeObject(new CheckMessage("회원가입 되었습니다.", true));
						} else {
							oos.writeObject(new CheckMessage("로그인이나 회원가입을 할 수 없습니다.", false));
							continue;
						}
						
						// 클라이언트 저장
						clientMap.put(LoginMsg.id, new Client(LoginMsg.id, clientSocket, ois, oos));
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});

		server.start();
		System.out.println("Server start - (서버를 종료하려면 '종료' 입력)");

		while (true) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			if(bf.readLine().equals("종료")) {
				serverSocket.close();
				server.interrupt();
			}
		}
	}
}
