package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;
import format.TypeInfo;

public class TypeInfoUpdate extends MessageObject{
	public TypeInfo[] info;
	
	public TypeInfoUpdate(TypeInfo[] info) {
		super(MessageTypeConstantNumbers.UPDATE_TYPE_INFO);
		this.info = info;
	}
}