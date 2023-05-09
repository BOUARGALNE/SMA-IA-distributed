package ma.enset.AES;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ClientAgentContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter("host","localhost");
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        String password="1234567812345678";//128
        AgentController agentControllerClient =agentContainer.createNewAgent("client","ma.enset.AES.AgentClient",new Object[]{password});
        agentControllerClient.start();
    }
}
