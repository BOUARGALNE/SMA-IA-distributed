package org.example.Agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import org.example.AgentsGuis.AgentServerGui;

import java.util.ArrayList;
import java.util.List;

public class AgentServer extends GuiAgent {
    private AgentServerGui agentServerGui;
    List<String> gamers=new ArrayList<>();
    private  int i=50;
    @Override
    protected void setup() {
        System.out.println("***  la méthode setup *****");
        agentServerGui=(AgentServerGui)getArguments()[0];
        agentServerGui.setAgentServer(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();

                if (receivedMSG!=null){
                    agentServerGui.showMessage(receivedMSG.getSender().getName().split("@")[0]+" : "+receivedMSG.getContent());
                    if(!gamers.contains((String)receivedMSG.getSender().getName().split("@")[0] )){
                        gamers.add(receivedMSG.getSender().getName().split("@")[0] );
                    }
                    //System.out.println(receivedMSG.getContent());
                    //System.out.println(receivedMSG.getSender().getName());
                    if ( Integer.parseInt(receivedMSG.getContent())>i){
                        ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(new AID(receivedMSG.getSender().getName().split("@")[0],AID.ISLOCALNAME));
                        message.setContent("ce n'est pas le nombre, entrer un nombre plus petit ");
                        send(message);
                    }else {
                        if(Integer.parseInt(receivedMSG.getContent())<i){
                            ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
                            message.addReceiver(new AID(receivedMSG.getSender().getName().split("@")[0],AID.ISLOCALNAME));
                            message.setContent("ce n'est pas le nombre, entrer un nombre plus grand ");
                            send(message);
                        }else {
                            for (String s:gamers) {
                                ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
                                message.addReceiver(new AID(s,AID.ISLOCALNAME));
                                message.setContent("Congratulation to "+receivedMSG.getSender().getName()+" a trouver le nombre magic qu'est "+i);
                                send(message);
                                ACLMessage message2=new ACLMessage(ACLMessage.REQUEST);
                                message2.addReceiver(new AID(s,AID.ISLOCALNAME));
                                message2.setContent("Game over");
                                send(message2);
                            }

                        }
                    }
                }else {
                    block();
                }

            }}
        );

    }

    @Override
    protected void beforeMove() {
        System.out.println("***  la méthode beforeMove *****");
    }

    @Override
    protected void afterMove() {
        System.out.println("***  la méthode afterMove *****");
    }

    @Override
    protected void takeDown() {
        System.out.println("***  la méthode takeDown *****");
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        String parameter =(String) guiEvent.getParameter(0);
        ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("Gamer1",AID.ISLOCALNAME));
        message.setContent(parameter);
        send(message);
    }
}
