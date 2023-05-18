package ma.enset;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class SimpleAgentContainer2 {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter("host","localhost");
        String password="1234567812345678";//128
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        AgentController agentControllerClient =agentContainer.createNewAgent("server","ma.enset.AgentServer",new Object[]{password});
        agentControllerClient.start();
    }
}
