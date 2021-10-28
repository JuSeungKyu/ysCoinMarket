package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class LoginCheckMessage extends MessageObject{
	public boolean check;
	public String msg;
	
	public LoginCheckMessage(String msg, boolean check) {
		super(MessageTypeConstantNumbers.LOGIN_CHECK_MSG);
		this.check = check;
		this.msg = msg;
	}
}
