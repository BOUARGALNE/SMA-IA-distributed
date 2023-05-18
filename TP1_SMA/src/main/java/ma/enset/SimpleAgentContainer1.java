package ma.enset;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class SimpleAgentContainer1 {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter("host","localhost");
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        String password="1234567812345678";//128
        AgentController agentControllerClient =agentContainer.createNewAgent("client","ma.enset.AgentClient",new Object[]{password});
        agentControllerClient.start();
    }
}
