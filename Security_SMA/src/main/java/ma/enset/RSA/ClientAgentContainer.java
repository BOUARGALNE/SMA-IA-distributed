package ma.enset.RSA;

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
        String encodedPbk="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIVpG0yzb75DBs8CBzqLYF89KQZmPQNWCdoNs+O1KbLjy2L1QI+7KGydyagH4YeK95Y/xiz+xih8JunhMGLAzbkCAwEAAQ==";//128
        AgentController agentControllerClient =agentContainer.createNewAgent("client","ma.enset.RSA.AgentClient",new Object[]{encodedPbk});
        agentControllerClient.start();
    }
}
