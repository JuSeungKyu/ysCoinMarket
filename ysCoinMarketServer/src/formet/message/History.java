package formet.message;

import java.util.ArrayList;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.PriceInfo;

public class History extends MessageObject{
	public PriceInfo[] info;
	
	public History(PriceInfo[] info) {
		super(MessageTypeConstantNumbers.HISTORY_LIST);
		this.info = info;
	}
}                  