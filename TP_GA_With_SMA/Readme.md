* crée et démarre plusieurs agents îles (IslandAgent) en fonction de la valeur de GAUtils.ISLAND_NUMBER, puis crée et démarre un agent maître (MasterAgent). Ces agents peuvent interagir et coopérer pour exécuter des tâches spécifiques dans le système multi-agents.
# MainContainer

    public class MainContainer extends Agent {
    public static void main(String[] args) throws ControllerException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter("gui","true");
        AgentContainer agentContainer=runtime.createMainContainer(profile);
        agentContainer.start();
     }
    }
#  IslandAgent 
la classe IslandAgent représente un agent  qui exécute un algorithme génétique. Il initialise la population, effectue des itérations d'opérations génétiques, envoie des messages contenant la fitness du meilleur individu et se désenregistre lorsque l'agent est arrêté.

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
             System.out.println("Iteration : "+iteration);
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
 # MasterAgent
 la classe MasterAgent représente l'agent maître qui est responsable de l'enregistrement du service et de la réception des messages des agents îles. L'agent maître affiche les informations des messages reçus et se désenregistre lorsque l'agent est arrêté.
 
     public class MasterAgent extends Agent {
    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription(); //1ère service
        serviceDescription.setName("master");
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receiver=receive();
                if (receiver!=null){
                    System.out.println("Agent : "+receiver.getSender().getName().split("@")[0]+" ** "+receiver.getContent());
                }
            }
        });
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

