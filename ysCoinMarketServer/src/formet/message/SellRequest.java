package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class SellRequest extends MessageObject{
	public String id;
	public String coinname;
	public int price;
	public int count;
	
	public SellRequest(String id, String coinname, int price, int count) {
		super(MessageTypeConstantNumbers.BUY_REQEUST);
		this.id = id;
		this.coinname = coinname;
		this.price= price; 
		this.count= count; 
	}
}                  