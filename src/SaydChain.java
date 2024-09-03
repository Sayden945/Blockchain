import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class SaydChain {

    public static ArrayList<Block> blockchain = new ArrayList<>(); // List of blocks
    public static int mineDifficulty = 5; // Difficulty of mining
    public static float minimumTransaction = 0.1f; // Minimum transaction amount
    public static HashMap<String, TxOutputs> UTXOs = new HashMap<>(); // List of unspent transactions
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {

        // Set Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Create wallets
        walletA = new Wallet();
        walletB = new Wallet();

        // Test key pairds
        System.out.println("Private and public keys:");
        System.out.println("Private key: " + StrUtil.getStringFromKey(walletA.privKey));
        System.out.println("Public key: " + StrUtil.getStringFromKey(walletA.pubKey));

        // Test transaction
        Tx tx = new Tx(walletA.pubKey, walletB.pubKey, 5, null);
        tx.genSignature(walletA.privKey);

        // Verify signature
        System.out.println("Is signature verified?");
        System.out.println(tx.verifySignature());
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

