package ma.enset.GA_SMA;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import ma.enset.GA_SMA.Genetic.GAUtils;
import ma.enset.GA_SMA.Genetic.GenticAlgorithm;


public class IslandAgent extends Agent {
    private GenticAlgorithm ga=new GenticAlgorithm();

    @Override
    protected void setup() {
        SequentialBehaviour sequentialBehaviour=new SequentialBehaviour();

        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ga.initialize();
                ga.sortPopulation();
            }
        });

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int iteration=1;
            @Override
            public void action() {
//                System.out.println("Iteration : "+iteration);
                ga.crossover();
                ga.mutation();
                ga.sortPopulation();
                iteration++;
            }
            @Override
            public boolean done() {
                return GAUtils.MAX_ITERATIONS==iteration ||  ga.getBestFintness()==GAUtils.CHROMOSOME_SIZE;
            }
        });

        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfAgentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription(); //1ère service
                serviceDescription.setType("ga");
                dfAgentDescription.addServices(serviceDescription);
                DFAgentDescription[] dfAgentDescriptions= null; //myAgent
                try {
                    dfAgentDescriptions = DFService.search(getAgent(), dfAgentDescription);
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }

                ACLMessage message=new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(dfAgentDescriptions[0].getName());
                message.setContent(String.valueOf(ga.getPopulation()[0].getFitness()));
                send(message);
            }
        });

        addBehaviour(sequentialBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }
}
