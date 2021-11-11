package format.message;

import java.util.ArrayList;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TransactionDetailsInfo;

public class TransactionDetailsMessage extends MessageObject{
	public ArrayList<TransactionDetailsInfo> info;
	
	public TransactionDetailsMessage(ArrayList<TransactionDetailsInfo> info) {
		super(MessageTypeConstantNumbers.TRANSACTION_DETAILS_UPDATE);
		this.info = info;
	}
}