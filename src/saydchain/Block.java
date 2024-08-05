package saydchain;

import java.util.Date;

public class Block {


    public String hash;
    public String previousHash;
    private String data; // Data of block.
    private final long timeStamp; // Timestamp of block creation
    private int nonce; //Random number used in mining
    public static int mineDifficulty = 5; //Difficulty of mining

    // Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calculate new hash based on blocks contents
    public String calculateHash() {
        return StrUtil.appSha256(previousHash + Long.toString(nonce) + data);
    }

    // Increases nonce value until hash target is reached.
    public void mineBlock(int mineDifficulty) {
        String target = new String(new char[mineDifficulty]).replace('\0', '0'); //Create a string with difficulty * "0"

        // Keep calculating the hash until it starts with the target string
        while (!hash.substring(0, mineDifficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Mined block: " + hash);
    }
}