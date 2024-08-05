import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class SaydChain {

    public static ArrayList<Block> blockchain = new ArrayList<>(); // List of blocks
    public static int mineDifficulty = 5; // Difficulty of mining

    public static void main(String[] args) {

        // Add blocks to the blockchain ArrayList
        blockchain.add(new Block("Hey, Im the Genesis block", "0"));
        System.out.println("Trying to mine block 1...");
        blockchain.get(0).mineBlock(mineDifficulty); // Mine the first block

        blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to mine block 2...");
        blockchain.get(1).mineBlock(mineDifficulty); // Mine the second block

        blockchain.add(new Block("third block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to mine block 3...");
        blockchain.get(2).mineBlock(mineDifficulty); // Mine the third block

        System.out.println("\nBlockchain is Valid: " + isValidChain());

        String bcJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("Blockchain: ");
        System.out.println(bcJson);
    }

    //
    public static boolean isValidChain() {
        Block curBlock;
        Block prevBlock;
        String hashTarget = new String(new char[mineDifficulty]).replace('\0', '0');


        // Loop to check hashes in blockchain
        for (int i = 1; i < blockchain.size(); i++) {
            curBlock = blockchain.get(i);
            prevBlock = blockchain.get(i - 1);

            // Compare registered hash and calculated hash
            if (!curBlock.hash.equals(curBlock.calculateHash())) {
                System.out.println("Invalid: Current Hashes not equal");
                return false;
            }

            // Compare previous hash and registered previous hash
            if (!prevBlock.hash.equals(curBlock.previousHash)) {
                System.out.println("Invalid: Previous Hashes not equal");
                return false;
            }

            // Check if the block has been mined
            if (!curBlock.hash.substring(0, mineDifficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }


}

