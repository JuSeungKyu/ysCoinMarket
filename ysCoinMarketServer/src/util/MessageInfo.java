package util;

import format.MessageObject;
import server.ClientManager;

public class MessageInfo {
	public ClientManager recipient;
	public MessageObject obj;
	
	public MessageInfo(ClientManager recipient, MessageObject obj) {
		this.recipient = recipient;
		this.obj = obj;
	}
}
