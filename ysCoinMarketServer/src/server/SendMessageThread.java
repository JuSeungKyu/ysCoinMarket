package server;

import java.util.LinkedList;
import java.util.Queue;

import format.MessageObject;
import util.MessageInfo;
import util.Util;

public class SendMessageThread implements Runnable {

	private static Queue<MessageInfo> messageQueue = new LinkedList<MessageInfo>();
	
	public static void addMessageQueue(ClientManager recipient, MessageObject obj) {
		messageQueue.add(new MessageInfo(recipient, obj));
	}
	
	private Util util;
	
	public SendMessageThread() {
		this.util = new Util();
	}
	
	@Override
	public void run() {
		while(true) {
			while(messageQueue.size() != 0) {
				MessageInfo msg = messageQueue.poll();
				msg.recipient.sendObject(msg.obj);
			}
			if(!util.sleep(10)) {
				return;
			}
		}
	}
}
