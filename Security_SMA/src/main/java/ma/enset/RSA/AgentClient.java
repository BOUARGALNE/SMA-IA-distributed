package ma.enset.RSA;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AgentClient extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                String message ="Hi Hamid";
                String encodedPrk=(String) getArguments()[0];
                byte[] decodedPrk = Base64.getDecoder().decode(encodedPrk);
                try {
                    ACLMessage aclMessage =new ACLMessage(ACLMessage.INFORM);
                    KeyFactory keyFactory=KeyFactory.getInstance("RSA");
                    PublicKey publicKey=keyFactory.generatePublic(new X509EncodedKeySpec(decodedPrk));
                    Cipher cipher=Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE,publicKey);
                    byte[] encryptMsg = cipher.doFinal(message.getBytes());
                    String encryptEncodedMsg = Base64.getEncoder().encodeToString(encryptMsg);
                    aclMessage.setContent(encryptEncodedMsg);
                    aclMessage.addReceiver(new AID("server",AID.ISLOCALNAME));
                    send(aclMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void afterMove() {
        System.out.println("*** client after move ***");
    }

    @Override
    protected void beforeMove() {
        System.out.println("*** client before move ***");
    }

    @Override
    protected void takeDown() {
        System.out.println("*** client takedown ***");
    }

}
