package ysCoin;

import java.util.Arrays;
import java.util.List;

import ysCoin.simulation.Simulation;

public class Main {

	public static void main(String[] args) {
		final int GENESIS_BLOCK_NUMBER = 1; //number of first block
        final int DIFFICULTY = 4; //required amount of zeros at the beginning of valid hash
        final int MAX_BLOCKS_AMOUNT = 5; //amount of blocks in chain

        List<String> transactionList = Arrays.asList(
                "First transaction",
                "Second transaction",
                "Third transaction",
                "Fourth transaction");

        Simulation.generateBlockchain(
                GENESIS_BLOCK_NUMBER,
                DIFFICULTY,
                transactionList,
                MAX_BLOCKS_AMOUNT);

        
	}

}
