public class TxInputs {
    public String txOutputId; // Reference to TransactionOutputs -> transactionId
    public TxOutputs UTXO; // Contains the Unspent transaction output (Bitcoin convention)

    public TxInputs(String txOutputId) {
        this.txOutputId = txOutputId;
    }
}
