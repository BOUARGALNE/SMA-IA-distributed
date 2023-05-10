package ma.enset.RSA;

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
        String encodedPrk="MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAhWkbTLNvvkMGzwIHOotgXz0pBmY9A1YJ2g2z47UpsuPLYvVAj7sobJ3JqAfhh4r3lj/GLP7GKHwm6eEwYsDNuQIDAQABAkA9slHxsrnmn1CywxlctcAnyz0BvJ5SwKNluBiNIeba11o+a4JYbTWjB/xjYSuU6QvKVyShjXs1R72MzLzoq1NRAiEAynypsYWMeR9JpJNNFLQYt5M31CCd7TeKX0GS8kws3Q8CIQCoqw18jyLGRlrfjcX+AGCBmLbMksNUGnFvXm3aK3S4twIgMWtJkz0P8sPTGmqlBmeKHMu+dXRcdvf2OpdrgN1cmbsCIE9ji8qIGMcYkGdO9NVUWhhFyCzMG5I3jBLucGhTMfJLAiAy6T9Hn7PxrF+1crrsbYU31MBvjLxuUo0b4raZIPq8Kw==";//128
        AgentContainer agentContainer= runtime.createAgentContainer(profile);
        AgentController agentControllerClient =agentContainer.createNewAgent("server","ma.enset.RSA.AgentServer",new Object[]{encodedPrk});
        agentControllerClient.start();
    }
}
