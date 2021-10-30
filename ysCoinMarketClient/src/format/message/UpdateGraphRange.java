package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class UpdateGraphRange extends MessageObject{
	public short[] range;
	
	public UpdateGraphRange(short[] range) {
		super(MessageTypeConstantNumbers.UPDATE_GRAPH_RANGE);
		this.range = range;
	}
}