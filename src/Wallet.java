import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PrivateKey privKey;
    public PublicKey pubKey;

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
}
