package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class CoinTypeChange extends MessageObject{
	public String coinId;
	
	public CoinTypeChange(String coinId) {
		super(MessageTypeConstantNumbers.CHAGNE_COIN_TYPE);
		this.coinId = coinId;
	}
}