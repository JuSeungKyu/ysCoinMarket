package format.message;

import java.util.ArrayList;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.PriceInfo;

public class History extends MessageObject{
	public PriceInfo[] info;
	
	public History(PriceInfo[] info) {
		super(MessageTypeConstantNumbers.HISTORY_LIST);
		this.info = info;
	}
}                  