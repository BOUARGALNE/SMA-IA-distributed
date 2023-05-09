package ma.enset.AES;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AgentServer extends Agent {
    @Override
    protected void setup() {
        String password=(String) getArguments()[0];
       addBehaviour(new CyclicBehaviour() {
           @Override
           public void action() {
               ACLMessage receive = receive();
               if (receive != null) {
                   String encryptEncodedMsg = receive.getContent();
                   // System.out.println(encryptEncodedMsg);
                   byte[] encryptMsg = Base64.getDecoder().decode(encryptEncodedMsg);
                   SecretKey secretKey = new SecretKeySpec(password.getBytes(), "AES");
                   try {
                       Cipher cipher = Cipher.getInstance("AES");
                       cipher.init(Cipher.DECRYPT_MODE,secretKey);
                       byte[] decrtptMsg = cipher.doFinal(encryptMsg);
                       System.out.println(new String(decrtptMsg));
                   } catch (Exception e) {
                       e.printStackTrace();

                   }
               }
           }
       });
       }
    @Override
    protected void afterMove() {
        System.out.println("*** server after move ***");
    }

    @Override
    protected void beforeMove() {
        System.out.println("*** server before move ***");
    }

    @Override
    protected void takeDown() {
        System.out.println("*** server takedown ***");
    }

}
