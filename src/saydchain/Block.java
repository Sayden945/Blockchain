package saydchain;

import java.util.Date;

public class Block {


    public String hash;
    public String previousHash;
    private String data; // Data of block.
    private final long timeStamp; // Timestamp of block creation
    private int nonce; //Random number used in mining
    public static int mineDifficulty = 5; //Difficulty of mining

    //Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /**
     * Calculates the hash for the block.
     * <p>
     * This method uses the SHA-256 algorithm to generate a hash based on the
     * previous block's hash, the current block's timestamp, and the block's data.
     *
     * @return The calculated hash as a String.
     */
    public String calculateHash() {
        return StrUtil.appSha256(previousHash + Long.toString(nonce) + data);
    }

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