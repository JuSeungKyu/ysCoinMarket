package coin;

import java.sql.Timestamp;
import java.util.List;

public class Block {
    Timestamp timestamp;
    String prevHashString;
    String validHashString;
    int difficulty;
    int nonce = 0;

    public Block(String prevHashString, int difficulty) {
        this.prevHashString = prevHashString;
        this.difficulty = difficulty;
        timestamp = new Timestamp(System.currentTimeMillis());
        
        ShaHash difficultyHash = new ShaHash(String.valueOf(difficulty));
        ShaHash timestampHash = new ShaHash(String.valueOf(timestamp));

        String baseHashString = difficultyHash.getHashedString() + timestampHash.getHashedString();

        miningHash(this.nonce, baseHashString, difficulty);
    }

    private void miningHash(int nonce, String baseHashString, int difficulty) {
        ShaHash possibleHash;
        ShaHash validHash;
        System.out.println("Mining in progress...\n");
        while (true) {
            possibleHash = new ShaHash(baseHashString + String.valueOf(nonce));
            if (validadeHash(possibleHash, difficulty) == true) {
                this.nonce = nonce;
                validHash = possibleHash;
                this.validHashString = validHash.getHashedString();
                break;
            } else
                nonce++;
        }
    }

    private boolean validadeHash(ShaHash possibleHash, int difficulty) {

        String possibleHashString = possibleHash.getHashedString();

        for (int i = 0; i <= difficulty; i++) {
            if (possibleHashString.charAt(i) != '0') {
                return false;
            }
        }

        return true;
    }

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getPrevHashString() {
		return prevHashString;
	}

	public void setPrevHashString(String prevHashString) {
		this.prevHashString = prevHashString;
	}

	public String getValidHashString() {
		return validHashString;
	}

	public void setValidHashString(String validHashString) {
		this.validHashString = validHashString;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
}
