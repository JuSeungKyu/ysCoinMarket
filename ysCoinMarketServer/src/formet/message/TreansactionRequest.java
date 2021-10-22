package formet.message;

import formet.MessageObject;
import formet.MessageTypeConstantNumbers;

public class TreansactionRequest extends MessageObject{
	public String id;
	public String[] text;
	
	public TreansactionRequest(String id, String[] text) {
		super(MessageTypeConstantNumbers.TRANSACTION_REQUEST);
		this.text = text;
		this.id = id;
	}
}                  