import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class StrUtil {
    //Applies Sha256 to a string and returns the result.
    public static String appSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            //Applies sha256 to our input
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexadecimal

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Applies ECDSA Signature and returns the result
    public static byte[] appECDSASignature(PrivateKey privKey, String input) {
        Signature dsa;
        byte[] output; // The output byte array
        try {
            // Initialize signature with ECDSA algorithm
            dsa = Signature.getInstance("ECDSA", "BC");

            dsa.initSign(privKey); // Initialize signature with private key
            byte[] strByte = input.getBytes(); // Convert input to bytes

            dsa.update(strByte); // Update signature with input

            output = dsa.sign(); // Set output to the signed input

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static Boolean verifyECDSASignature(PublicKey pubKey, String data, byte[] signature) {
        try {
            // Initialize signature with ECDSA algorithm
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");

            ecdsaVerify.initVerify(pubKey); // Initialize signature with public key
            ecdsaVerify.update(data.getBytes()); // Update signature with data

            return ecdsaVerify.verify(signature); // Verify the signature
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}