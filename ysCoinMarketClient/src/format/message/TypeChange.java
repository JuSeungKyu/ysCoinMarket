package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class TypeChange extends MessageObject{
	public String coinId;
	public String historyBlock;
	
	public TypeChange(String coinId, String historyBlock) {
		super(MessageTypeConstantNumbers.TYPE_CHANGE);
		this.coinId = coinId;
		this.historyBlock = historyBlock;
	}
}