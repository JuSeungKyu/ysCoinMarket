package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class CheckMessage extends MessageObject{
	boolean check;
	String msg;
	
	public CheckMessage(String msg, boolean check) {
		super(MessageTypeConstantNumbers.CHECK_MSG);
		this.check = check;
		this.msg = msg;
	}
}
