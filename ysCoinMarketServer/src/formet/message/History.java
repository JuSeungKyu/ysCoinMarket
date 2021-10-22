package formet.message;

import java.util.ArrayList;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.PriceInfo;

public class History extends MessageObject{
	public ArrayList<PriceInfo> info;
	
	public History(ArrayList<PriceInfo> info) {
		super(MessageTypeConstantNumbers.HISTORY_LIST);
		this.info = info;
	}
}                  