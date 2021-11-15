package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class PreviousHashMessage extends MessageObject {

	public String hash;

	public PreviousHashMessage(String hash) {
		super(MessageTypeConstantNumbers.PREVIOUS_HASH_MESSAGE);
		this.hash = hash;
	}

}
