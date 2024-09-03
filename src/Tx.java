import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Tx {
    private static int sequence = 0; // Counter for number of transactions
    public String transactionId; // ID/Hash of transaction
    public PublicKey sender; // Senders address
    public PublicKey recipient; // Recipients address
    public float value; // Value of transaction
    public byte[] signature; // Signature of transaction for validation
    public ArrayList<TxInputs> inputs = new ArrayList<>(); // List of inputs
    public ArrayList<TxOutputs> outputs = new ArrayList<>(); // List of outputs

    // Constructor
    public Tx(PublicKey from, PublicKey to, float value, ArrayList<TxInputs> inputs) {
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

    // Process transaction, true if successful
    public boolean processTx() {
        if (verifySignature() == false) {
            System.out.println("# Transaction Signature failed to verify");
            return false;
        }

        // Gather transaction inputs
        for (TxInputs i : inputs) {
            i.UTXO = SaydChain.UTXOs.get(i.txOutputId);
        }

        // Check if valid transaction
        if (getInputsValue() < SaydChain.minimumTransaction) {
            System.out.println("# Transaction Inputs too small: " + getInputsValue());
            return false;
        }

        // Generate outputs
        float valueLeft = getInputsValue() - value; // Calculate value left after transaction
        transactionId = calculateHash(); // Generate transaction ID
        outputs.add(new TxOutputs(this.recipient, value, transactionId)); // Add value to recipient
        outputs.add(new TxOutputs(this.sender, valueLeft, transactionId)); // Add remaining value to sender

        // Add outputs to unspent transactions
        for (TxOutputs o : outputs) {
            SaydChain.UTXOs.put(o.id, o);
        }

        // Remove transaction inputs from UTXOs
        for (TxInputs i : inputs) {
            if (i.UTXO == null) continue;
            SaydChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    // Returns sum of inputs values
    public float getInputsValue() {
        float total = 0;

        // Loop through inputs and add to total
        for (TxInputs i : inputs) {
            if (i.UTXO == null) continue;
            total += i.UTXO.value;
        }

        return total;
    }

    // Returns sum of outputs values
    public float getOutputsValue() {
        float total = 0;

        // Loop through outputs and add to total
        for (TxOutputs o : outputs) {
            total += o.value;
        }
        return total;
    }
}
