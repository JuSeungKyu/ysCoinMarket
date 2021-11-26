package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TypeInfo;

public class UserInfoMsg extends MessageObject{
	public long money;
	public long count;
	public byte fee;
	
	public UserInfoMsg(long money, long count, byte fee) {
		super(MessageTypeConstantNumbers.USER_INFO_MSG);
		this.money = money;
		this.count = count;
		this.fee = fee;
	}
}