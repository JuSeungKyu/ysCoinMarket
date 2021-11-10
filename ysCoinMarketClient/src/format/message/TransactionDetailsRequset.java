package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class TransactionDetailsRequset extends MessageObject{
	public TransactionDetailsRequset() {
		super(MessageTypeConstantNumbers.TRANSACTION_DETAILS_REQUEST);
	}
}