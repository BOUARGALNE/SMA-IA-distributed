package ma.enset.AES;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AgentClient extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                String message ="Hi Hamid";
                String password=(String) getArguments()[0];
                SecretKey secretKey=new SecretKeySpec(password.getBytes(),"AES");
                try {
                    ACLMessage aclMessage =new ACLMessage(ACLMessage.INFORM);
                    Cipher cipher =Cipher.getInstance("AES");
                    cipher.init(Cipher.ENCRYPT_MODE,secretKey);
                    byte[] encryptMSG = cipher.doFinal(message.getBytes());
                    String encryptEncodedMSG = Base64.getEncoder().encodeToString(encryptMSG);
                    aclMessage.setContent(encryptEncodedMSG);
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
