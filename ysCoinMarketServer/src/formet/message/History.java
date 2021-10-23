package formet.message;

import java.util.ArrayList;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;
import formet.PriceInfo;

public class History extends MessageObject{
	public PriceInfo[] info;
	public String coinName;
	
	public History(PriceInfo[] info, String coinName) {
		super(MessageTypeConstantNumbers.HISTORY_LIST);
		this.info = info;
		this.coinName = coinName;
	}
}                  