package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class CheckMessage extends MessageObject{
	public boolean check;
	public String msg;
	
	public CheckMessage(String msg, boolean check) {
		super(MessageTypeConstantNumbers.CHECK_MSG);
		this.check = check;
		this.msg = msg;
	}
}
