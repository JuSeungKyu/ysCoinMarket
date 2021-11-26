package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import db.query.UtilQuery;
import format.message.LoginCheckMessage;
import format.message.LoginRequest;

public class UserLoginManager implements Runnable{
	ServerSocket serverSocket;
	UtilQuery utilQuery;
	
	public UserLoginManager(ServerSocket serverSocket, UtilQuery utilQuery) {
		this.serverSocket = serverSocket;
		this.utilQuery = utilQuery;
	}
	
	@Override
	public void run() {
		// 서버 소켓 설정
		while (true) {
			try {
				// 클라이언트 받음
				Socket clientSocket = this.serverSocket.accept();

				// 인, 아웃풋 스트림 준비
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

				// 아이디 받음
				LoginRequest LoginMsg = (LoginRequest) ois.readObject();
				
				// 로그인 되어 있는 아이디인지 체크
				if(LoginMsg.isLogin && Server.clientMap.get(LoginMsg.id) != null) {
					oos.writeObject(new LoginCheckMessage("이미 같은 아이디로 로그인 하였습니다.", false));
					continue;
				}
				
				// 존재하는 아이디인지 확인
				if(LoginMsg.isLogin) {
					int idCount = (int) ((long) this.utilQuery.justGetObject("SELECT count(id) FROM users WHERE id = '"
							+ LoginMsg.id + "'AND pw = '" + LoginMsg.pw + "'"));
					if(idCount != 0) {
						oos.writeObject(new LoginCheckMessage("로그인 되었습니다.", true));
					} else {
						oos.writeObject(new LoginCheckMessage("로그인 실패", false));
						continue;
					}
				} else {
					int idCount = (int) ((long) this.utilQuery.justGetObject("SELECT count(id) FROM users WHERE id = '"
							+ LoginMsg.id + "'"));
					if(idCount == 0) {
						this.utilQuery.justUpdate("INSERT INTO users(`id`, `pw`) VALUES ('" + LoginMsg.id + "','"
							+ LoginMsg.pw + "')");
						oos.writeObject(new LoginCheckMessage("회원가입 되었습니다.", true));
					} else {
						oos.writeObject(new LoginCheckMessage("회원가입 실패", false));
						continue;
					}
				}
				
				// 클라이언트 저장
				System.out.println(LoginMsg.id+"님이 로그인 하셨습니다.");
				Server.clientIdList.add(LoginMsg.id);
				Server.clientMap.put(LoginMsg.id, new ClientManager(LoginMsg.id, clientSocket, ois, oos));
			} catch (Exception e) {
				System.out.println("유저와 통신할 수 없습니다");
			}
		}
	}
}
