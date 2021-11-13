package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.QuoteInfo;
import format.TypeInfo;

public class QuoteInfoUpdate extends MessageObject{
	public QuoteInfo[] info;
	
	public QuoteInfoUpdate(QuoteInfo[] info) {
		super(MessageTypeConstantNumbers.UPDATE_QUOTE_INFO);
		this.info = info;
	}
}
