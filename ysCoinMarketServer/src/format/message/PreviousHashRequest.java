package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class PreviousHashRequest extends MessageObject {

	public String coinId = null;
	
	public PreviousHashRequest(String coinId) {
		super(MessageTypeConstantNumbers.PREVIOUS_HASH_REQUEST);
		this.coinId = coinId;
	}

}
