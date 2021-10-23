package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class BuyRequest extends MessageObject{
	public String id;
	public int price;
	public int count;
	
	public BuyRequest(String id, int price, int count) {
		super(MessageTypeConstantNumbers.BUY_REQEUST);
		this.id = id;
		this.price= price; 
		this.count= count; 
	}
}                  