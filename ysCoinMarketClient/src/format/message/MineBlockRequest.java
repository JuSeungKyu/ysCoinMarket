package format.message;

import format.MessageObject;
import format.MessageTypeConstantNumbers;

public class MineBlockRequest extends MessageObject {
	public String hash;
	public String coinId;
	
	public MineBlockRequest(String hash, String coinId) {
		super(MessageTypeConstantNumbers.BLOCK_MINE_REQUEST);
		this.hash = hash;
		this.coinId = coinId;
	}
}
