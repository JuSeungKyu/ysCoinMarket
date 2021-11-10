package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;

public class TransactionDetailsRequest extends MessageObject{
	public TransactionDetailsInfo[] info;
	
	public TransactionDetailsRequest(TransactionDetailsInfo[] info) {
		super(MessageTypeConstantNumbers.TRANSACTION_DETAILS_REQUEST);
		this.info = info;
	}
}