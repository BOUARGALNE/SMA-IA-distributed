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
  # SimpleContainer
  la classe SimpleContainer est responsable de la création et du démarrage du conteneur d'agents JADE. Elle crée plusieurs instances de l'agent IslandAgent et une instance de l'agent MasterAgent, puis les démarre dans le conteneur. Cela permet de mettre en place un système multi-agents où les agents îles effectuent des opérations génétiques à l'aide de l'algorithme génétique, tandis que l'agent maître coordonne et surveille leur activité.
  
         public class SimpleContainer  {
         public static void main(String[] args) throws ControllerException {
         Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        for (int i = 0; i < GAUtils.ISLAND_NUMBER; i++) {
            AgentController islandAgent = agentContainer.createNewAgent("IslandAgent" + i, IslandAgent.class.getName(), new Object[]{});
            islandAgent.start();
        }
        AgentController masterAgent = agentContainer.createNewAgent("MasterAgent", MasterAgent.class.getName(), new Object[]{});
        masterAgent.start();
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
# GenticAlgorithm


     public class GenticAlgorithm {
    private Individual[] population=new Individual[GAUtils.POPULATION_SIZE];
    private Individual individual1;
    public Individual individual2;
    public void initialize(){
        for (int i=0;i<GAUtils.POPULATION_SIZE;i++) {
            population[i]=new Individual();
            population[i].calculateFintess();
        }
    }

    public void crossover(){
        individual1=new Individual(population[0].getChromosome());
        individual2=new Individual(population[1].getChromosome());

        Random random=new Random();
        int crossPoint=random.nextInt(GAUtils.CHROMOSOME_SIZE-1);
        crossPoint++;
        for (int i = 0; i <crossPoint ; i++) {
            individual1.getChromosome()[i]=population[1].getChromosome()[i];
            individual2.getChromosome()[i]=population[0].getChromosome()[i];
        }


     //   System.out.println("crossover point "+crossPoint);
      //  System.out.println("Individual 1 "+Arrays.toString(individual1.getChromosome()));
       // System.out.println("Individual 2 "+Arrays.toString(individual2.getChromosome()));
    }
    public void showPopulation(){
        for (Individual individual:population) {
            System.out.println(Arrays.toString(individual.getChromosome())+" = "+individual.getFitness());
        }
    }
    public void sortPopulation(){
        Arrays.sort(population, Comparator.reverseOrder());
    }
    public void mutation(){
        Random random=new Random();
        // individual1
        if(random.nextDouble()>GAUtils.MUTATION_PROP){
            int index = random.nextInt(GAUtils.CHROMOSOME_SIZE);
            individual1.getChromosome()[index]=GAUtils.ALPHAS.charAt(random.nextInt(GAUtils.ALPHAS.length()));
        }
        // individual2
        if(random.nextDouble()>GAUtils.MUTATION_PROP){
            int index = random.nextInt(GAUtils.CHROMOSOME_SIZE);
            individual2.getChromosome()[index]=GAUtils.ALPHAS.charAt(random.nextInt(GAUtils.ALPHAS.length()));
        }
        // System.out.println("Après mutation ");
        // System.out.println("Individual 1 "+Arrays.toString(individual1.getChromosome()));
        //System.out.println("Individual 2 "+Arrays.toString(individual2.getChromosome()));
        individual1.calculateFintess();
        individual2.calculateFintess();
        population[GAUtils.POPULATION_SIZE-2]=individual1;
        population[GAUtils.POPULATION_SIZE-1]=individual2;


    }
    public int getBestFintness(){
        return population[0].getFitness();
    }
    public Individual[] getPopulation() {
        return population;
    }
    }
   # Individual

    public class Individual implements Comparable{
    private char []chromosome=new char[GAUtils.CHROMOSOME_SIZE];
    private int fitness=0;

    public Individual() {
        Random random = new Random();
        for (int i = 0; i < GAUtils.CHROMOSOME_SIZE; i++) {
            chromosome[i] = GAUtils.ALPHAS.charAt(random.nextInt(GAUtils.ALPHAS.length()));
        }
    }

    public Individual(char[] chromosome) {
        this.chromosome = Arrays.copyOf(chromosome,GAUtils.CHROMOSOME_SIZE);
    }

    public void calculateFintess(){
        for (int i=0;i<GAUtils.CHROMOSOME_SIZE;i++) {
            if(chromosome[i]==GAUtils.objectif.charAt(i)){
                fitness+=1;
            }

        }
    }

    public int getFitness() {
        return fitness;
    }

    public char[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(char[] chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual=(Individual) o;
        if (this.fitness>individual.fitness){
            return  1;
        }else if(this.fitness< individual.fitness){
            return -1;
        }else{
            return 0;
        }
    }
  }
# GAUtils
    public class GAUtils {
    public static final int CHROMOSOME_SIZE=12;
    public static final int POPULATION_SIZE=40;
    public static final double MUTATION_PROP=0.2;
    public static final int MAX_ITERATIONS=2000;
    public static final String objectif="bonjour BDCC";
    public static final String ALPHAS="abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int ISLAND_NUMBER=5;
}
