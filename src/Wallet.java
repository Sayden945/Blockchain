import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privKey;
    public PublicKey pubKey;

    public HashMap<String, TxOutputs> UTXOs = new HashMap<String, TxOutputs>(); // Unspent transactions

    // Constructor
    public Wallet() {
        generateKeyPair();
    }

    // Generate key pair
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC"); // Elliptic Curve Digital Signature Algorithm
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); // Secure random number generator
            ECGenParameterSpec ecParSpec = new ECGenParameterSpec("prime192v1"); // Elliptic curve domain parameters

            // Initialize key generator and generate pair
            keyGen.initialize(ecParSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set public and private keypair
            privKey = keyPair.getPrivate();
            pubKey = keyPair.getPublic();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Get balance
    public float getBalance() {
        float total = 0;

        // Loop through UTXOs and add to balance if they belong to this wallet
        for (Map.Entry<String, TxOutputs> item : SaydChain.UTXOs.entrySet()) {
            TxOutputs UTXO = item.getValue();
            if (UTXO.isMine(pubKey)) {
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }

        return total; // Return total balance
    }

    public Tx sendFunds(PublicKey _recipient, float value) {
        // Check if balance is enough to send
        if (getBalance() < value) {
            System.out.println("# Not Enough funds to send, transaction Aborted.");
            return null;
        }

        // Create array list of inputs
        ArrayList<TxInputs> inputs = new ArrayList<TxInputs>();

        float total = 0;

        // Loop through UTXOs and add to inputs until enough value is reached
        for (Map.Entry<String, TxOutputs> item : UTXOs.entrySet()) {
            TxOutputs UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TxInputs(UTXO.id));
            if (total > value) break;
        }

        // Create new transaction
        Tx newTx = new Tx(pubKey, _recipient, value, inputs);
        newTx.genSignature(privKey);

        for (TxInputs input : inputs) {
            UTXOs.remove(input.txOutputId); // Remove input from UTXOs
        }
        return newTx;
    }
}