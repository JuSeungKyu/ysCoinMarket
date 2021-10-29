package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class CoinTypeChange extends MessageObject{
	public String coinId;
	public String historyBlock;
	
	public CoinTypeChange(String coinId, String historyBlock) {
		super(MessageTypeConstantNumbers.CHAGNE_COIN_TYPE);
		this.coinId = coinId;
		this.historyBlock = historyBlock;
	}
}