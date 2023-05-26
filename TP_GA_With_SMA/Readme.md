* crée et démarre plusieurs agents îles (IslandAgent) en fonction de la valeur de GAUtils.ISLAND_NUMBER, puis crée et démarre un agent maître (MasterAgent). Ces agents peuvent interagir et coopérer pour exécuter des tâches spécifiques dans le système multi-agents.
[public class SimpleContainer  {
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
    }](https://github.com/BOUARGALNE/SMA-IA-distributed/blob/8fbb191cd22b31809e5ce78ad1f18cdf9e46a8ef/TP_GA_With_SMA/src/ma/enset/GA_SMA/IslandAgent.java#L20)

}
