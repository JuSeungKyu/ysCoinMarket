package formet;

import java.io.Serializable;

public class MessageObject implements Serializable {
	public byte type;
	public MessageObject(byte type) {
		this.type = type;
	}
}
