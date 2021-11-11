package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;

public class TransactionDetailsRequest extends MessageObject{
	public TransactionDetailsRequest() {
		super(MessageTypeConstantNumbers.TRANSACTION_DETAILS_REQUEST);
	}
}