package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class SellRequest extends MessageObject{
	public String coinname;
	public int price;
	public int count;
	
	public SellRequest(String coinname, int price, int count) {
		super(MessageTypeConstantNumbers.SELL_REQEUST);
		this.coinname = coinname;
		this.price= price; 
		this.count= count; 
	}
}