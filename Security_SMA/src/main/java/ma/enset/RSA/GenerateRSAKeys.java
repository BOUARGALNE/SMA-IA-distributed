package ma.enset.RSA;

import java.security.*;
import java.util.Base64;

public class GenerateRSAKeys {
    public static void main(String[] args) {
        try {
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair=keyPairGenerator.generateKeyPair();
            PrivateKey privateKey=keyPair.getPrivate();
            PublicKey publicKey=keyPair.getPublic();
            String encodedPRK = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            String encodedPPK = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("private key ;" +encodedPRK);
            System.out.println("public key : "+encodedPPK);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
