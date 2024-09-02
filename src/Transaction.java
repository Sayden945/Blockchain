import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    private static int sequence = 0; // Counter for number of transactions
    public String transactionId; // ID/Hash of transaction
    public PublicKey sender; // Senders address
    public PublicKey recipient; // Recipients address
    public float value; // Value of transaction
    public byte[] signature; // Signature of transaction for validation
    public ArrayList<TransactionInput> inputs = new ArrayList<>(); // List of inputs
    public ArrayList<TransactionOutput> outputs = new ArrayList<>(); // List of outputs

    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // Calculate transaction hash
    private String calculateHash() {
        sequence++; // Increase sequence to avoid identical transactions
        return StrUtil.appSha256(
                StrUtil.getStringFromKey(sender) +
                        StrUtil.getStringFromKey(recipient) +
                        Float.toString(value) +
                        sequence
        );
    }

    // Sign transaction with private key to prevent tampering
    public void genSignature(PrivateKey privKey) {
        String data = StrUtil.getStringFromKey(sender) + StrUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StrUtil.appECDSASignature(privKey, data);
    }

    // Verify data integrity
    public boolean verifySignature() {
        String data = StrUtil.getStringFromKey(sender) + StrUtil.getStringFromKey(recipient) + Float.toString(value);
        return StrUtil.verifyECDSASignature(sender, data, signature);
    }
}
