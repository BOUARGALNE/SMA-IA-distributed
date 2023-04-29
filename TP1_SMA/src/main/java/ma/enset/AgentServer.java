package ma.enset;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AgentServer extends Agent {
    @Override
    protected void setup() {
        System.out.println("*** server setup ***");
        ACLMessage receivedMSG = blockingReceive();
        if (receivedMSG!=null){
            System.out.println(receivedMSG.getContent());
            System.out.println(receivedMSG.getSender().getName());
        }
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
