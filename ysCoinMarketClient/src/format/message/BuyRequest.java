package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class BuyRequest extends MessageObject{
	public String coinname;
	public int price;
	public int count;
	
	public BuyRequest(String coinname, int price, int count) {
		super(MessageTypeConstantNumbers.BUY_REQEUST);
		this.coinname = coinname;
		this.price= price; 
		this.count= count; 
	}
}                  