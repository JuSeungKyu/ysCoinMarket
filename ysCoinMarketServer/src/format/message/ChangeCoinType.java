package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.PriceInfo;

public class ChangeCoinType extends MessageObject{
	public String coinId;
	
	public ChangeCoinType(String coinId) {
		super(MessageTypeConstantNumbers.CHAGNE_COIN_TYPE);
		this.coinId = coinId;
	}
}
