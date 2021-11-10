package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;

public class TransactionDetailsMessage extends MessageObject{
	public TransactionDetailsInfo[] info;
	
	public TransactionDetailsMessage(TransactionDetailsInfo[] info) {
		super(MessageTypeConstantNumbers.TRANSACTION_DETAILS_UPDATE);
		this.info = info;
	}
}