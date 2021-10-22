package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class LoginRequest extends MessageObject{
	public String id;
	public String pw;
	public boolean isLogin;
	
	public LoginRequest(String id, String pw, boolean isLogin) {
		super(MessageTypeConstantNumbers.LOGIN);
		this.id = id;
		this.pw = pw;
		this.isLogin = isLogin;
	}
}                  