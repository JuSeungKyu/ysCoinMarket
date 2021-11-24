package format.message;

import java.util.ArrayList;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class UpdateOrderBook extends MessageObject{
	public ArrayList<int[]> info;
	public UpdateOrderBook(ArrayList<int[]> info) {
		super(MessageTypeConstantNumbers.UPDATE_ORDER_BOOK);
		this.info = info;
	}
}
