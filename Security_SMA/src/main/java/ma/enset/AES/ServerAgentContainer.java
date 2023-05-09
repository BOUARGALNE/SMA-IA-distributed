package ma.enset.AES;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ServerAgentContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter("host","localhost");
        String password="1234567812345678";//128
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        AgentController agentControllerClient =agentContainer.createNewAgent("server","ma.enset.AES.AgentServer",new Object[]{password});
        agentControllerClient.start();
    }
}
