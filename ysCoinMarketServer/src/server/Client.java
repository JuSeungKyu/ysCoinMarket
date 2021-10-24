package server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.ws.handler.MessageContext;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class Client extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String id;
	private Socket socket;
	private boolean isReady; 

	public Client(String id, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.id = id;
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	public void sendMessage(MessageObject obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		try {
			while (true) {
				try {
					MessageObject msg = (MessageObject) ois.readObject();

					if (msg == null) {
						break;
					}
					
					if (msg.type == MessageTypeConstantNumbers.BUY_REQEUST) {
						
					}
					
					if (msg.type == MessageTypeConstantNumbers.SELL_REQEUST) {
						
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				Server.clientMap.remove(id);
				for(int i = 0; i < Server.clientIdList.size(); i++) {
					if(Server.clientIdList.get(i).equals(this.id)) {
						Server.clientIdList.remove(i);
						break;
					}
				}
				removeClient();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeClient() {
		Server.clientMap.remove(id);
		for(int i = 0; i < Server.clientIdList.size(); i++) {
			if(Server.clientIdList.get(i).equals(this.id)) {
				Server.clientIdList.remove(i);
				break;
			}
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendObject(Object object) {
		try {
			this.oos.writeObject(object);
			this.oos.flush();
		} catch (IOException e) {
			removeClient();
		}
	}
	
	public boolean isReady() {
		return this.isReady;
	}
}
