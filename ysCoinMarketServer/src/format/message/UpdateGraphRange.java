package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class UpdateGraphRange extends MessageObject{
	public short[] range;
	
	public UpdateGraphRange(short[] range) {
		super(MessageTypeConstantNumbers.UPDATE_GRAPH_RANGE);
		this.range = range;
	}
	
	public UpdateGraphRange(short range) {
		super(MessageTypeConstantNumbers.UPDATE_GRAPH_RANGE);
		this.range = new short[2];
		this.range[0] = range;
		this.range[1] = range;
	}
}