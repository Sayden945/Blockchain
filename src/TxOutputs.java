import java.security.PublicKey;

public class TxOutputs {
    public String id;
    public PublicKey recipient; // Recipient of transaction
    public float value; // Value of transaction
    public String parentTransactionId; // ID of parent transaction

    // Constructor
    public TxOutputs(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StrUtil.appSha256(StrUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    // Check if currency is owned by you
    public Boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }
}
