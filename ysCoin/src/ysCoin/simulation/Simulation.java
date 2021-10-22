package ysCoin.simulation;

import java.util.List;

import ysCoin.block.Block;

public class Simulation {
	static final String genesisPrevHashString = "4e17811011ec217ac84a9e037a82758a7de318342886a88a37ebabf90f52af73";

    static public void generateBlockchain(
            int blockNumber,
            int difficulty,
            List<String> transactionList,
            int maxBlocksAmount) {

        System.out.println("SIMPLE BLOCKCHAIN:\n" +
                "Difficulty: " + difficulty + "\n" +
                "List of transactions is constant for every block\n");

        Block previousBlock = generateGenesisBlock(blockNumber, difficulty, transactionList);

        for (blockNumber = 2; blockNumber <= maxBlocksAmount; blockNumber++) {
            Block block = new Block(blockNumber,
                    previousBlock.getValidHashString(),
                    difficulty,
                    transactionList);

            previousBlock = block;

            System.out.println(
                    "New Block added:\n" +
                            "Block number: " + blockNumber + "\n" +
                            "Timestamp: " + block.getTimestamp() + "\n" +
                            "Previous hash: " + block.getPrevHashString() + "\n" +
                            "Nonce: " + block.getNonce() + "\n" +
                            "Valid block hash: " + block.getValidHashString() + "\n");
        }
    }

    static public Block generateGenesisBlock(int blockNumber, int difficulty, List<String> transactionList) {
    	
        Block genesisBlock = new Block(
                blockNumber,
                genesisPrevHashString,
                difficulty,
                transactionList);

        System.out.println(
                "Genesis Block:\n" +
                        "Block number: " + blockNumber + "\n" +
                        "Timestamp: " + genesisBlock.getTimestamp() + "\n" +
                        "Previous hash: " + genesisPrevHashString + "\n" +
                        "Nonce: " + genesisBlock.getNonce() + "\n" +
                        "Valid block hash: " + genesisBlock.getValidHashString() + "\n");

        return genesisBlock;
    }

}
