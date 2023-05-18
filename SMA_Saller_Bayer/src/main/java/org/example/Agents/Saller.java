package org.example.Agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import org.example.AgentsGuis.AgentSaller_Gui;

public class Saller extends GuiAgent {
    private AgentSaller_Gui clientGui;
    @Override
    protected void setup() {
        System.out.println("*** Client: la méthode setup *****");
        clientGui=(AgentSaller_Gui) getArguments()[0];
       clientGui.setAgentGamer1(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();
                if (receivedMSG!=null){
                    clientGui.showMessage(receivedMSG.getSender().getName().split("@")[0]+" : "+receivedMSG.getContent());
                    System.out.println(receivedMSG.getContent());
                    System.out.println(receivedMSG.getSender().getName());
                }else {
                    block();
                }

            }}
        );
        /*ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
        message.setContent("bonjour serveur ce message et pour demander un service");
        message.addReceiver(new AID("server",AID.ISLOCALNAME));
        send(message);*/
        //addBehaviour(new );
        //generic behaviour
        /*addBehaviour(new Behaviour() {
            private int conteur;
            @Override
            public void action() {
                System.out.println("**** Contneur = "+conteur+" *****");
                conteur++;
            }

            @Override
            public boolean done() {
                return conteur==10?true:false;
            }
        });*/
       /* addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("**** One Shot Behaviour ******");
            }
        });*/
        /*addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println("**** Cyclic Behaviour *******");
            }
        });*/
      /*addBehaviour(new TickerBehaviour(this,5000) {
          @Override
          protected void onTick() {
              System.out.println("***** Ticker Behaviour *****");
          }
      });*/
       /* addBehaviour(new WakerBehaviour(this,5000) {
            @Override
            protected void onWake() {
                System.out.println("**** Waker Behaviour ****");
            }
        });*/
       /* ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println("***** Cyclic Behaviour 1 **** ");
            }
        });
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println("***** Cyclic Behaviour 2 **** ");
            }
        });
        addBehaviour(parallelBehaviour);*/
    }

    @Override
    protected void beforeMove() {
        System.out.println("*** Client:  la méthode beforeMove *****");
    }

    @Override
    protected void afterMove() {
        System.out.println("*** Client: la méthode afterMove *****");
    }

    @Override
    protected void takeDown() {
        System.out.println("*** Client: la méthode takeDown *****");
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        String parameter =(String) guiEvent.getParameter(0);
        ACLMessage message=new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("server",AID.ISLOCALNAME));
        message.setContent(parameter);
        send(message);
    }
}
