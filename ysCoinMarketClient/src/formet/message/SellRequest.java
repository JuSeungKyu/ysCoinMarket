package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class SellRequest extends MessageObject{
	public String id;
	public int price;
	public int count;
	
	public SellRequest(String id, int price, int count) {
		super(MessageTypeConstantNumbers.SELL_REQEUST);
		this.id = id;
		this.price= price; 
		this.count= count; 
	}
}                  