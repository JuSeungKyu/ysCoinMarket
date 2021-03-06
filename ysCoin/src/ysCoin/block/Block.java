package ysCoin.block;

import java.sql.Timestamp;
import java.util.List;

import ysCoin.merkleroot.MerkleTree;
import ysCoin.shahash.ShaHash;

public class Block {
	int blockNumber;
    Timestamp timestamp;
    String prevHashString;
    String validHashString;
    int difficulty;
    int nonce = 0;
    List<String> transactionsList;

    public Block(int blockNumber, String prevHashString, int difficulty, List<String> transactionsList) {
        this.blockNumber = blockNumber;
        this.prevHashString = prevHashString;
        this.difficulty = difficulty;
        this.transactionsList = transactionsList;
        timestamp = new Timestamp(System.currentTimeMillis());

        ShaHash blockNumberHash = new ShaHash(String.valueOf(blockNumber));
        ShaHash difficultyHash = new ShaHash(String.valueOf(difficulty));
        ShaHash timestampHash = new ShaHash(String.valueOf(timestamp));

        MerkleTree merkleTree = new MerkleTree(transactionsList);
        MerkleTree.Node rootNode = merkleTree.getRoot();
        ShaHash merkleRootHash = new ShaHash(String.valueOf(rootNode.getSig()));

        String baseHashString = blockNumberHash.getHashedString()
                + difficultyHash.getHashedString()
                + timestampHash.getHashedString()
                + merkleRootHash.getHashedString();

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

	public int getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
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

	public List<String> getTransactionsList() {
		return transactionsList;
	}

	public void setTransactionsList(List<String> transactionsList) {
		this.transactionsList = transactionsList;
	}

}
