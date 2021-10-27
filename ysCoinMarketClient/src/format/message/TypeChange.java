package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class TypeChange extends MessageObject{
	public String coinId;
	
	public TypeChange(String coinId) {
		super(MessageTypeConstantNumbers.TYPE_CHANGE);
		this.coinId = coinId;
	}
}