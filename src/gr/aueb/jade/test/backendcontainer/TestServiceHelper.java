package gr.aueb.jade.test.backendcontainer;

import gr.aueb.jade.test.util.JadeUtility;
import jade.core.AID;
import jade.core.Agent;
import jade.core.BackEndContainer;
import jade.core.ServiceHelper;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class TestServiceHelper implements ServiceHelper {

	BackEndContainer backEndContainer;
	Agent myAgent;
	public TestServiceHelper(BackEndContainer bec, Agent a) {
		this.backEndContainer = bec;
		this.myAgent = a;
	}
	
	@Override
	public void init(Agent a) {
	}

	public boolean postMessageToLocalAgent(){
		
		System.out.println("Executing Testing service HELPER (postMessageToLocalAgent) ...." + myAgent);
		
		AID pongAID = new AID("pong", false);
		
		ACLMessage msg =  new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.addReceiver(pongAID);
		msg.setContent("ping");
		
		boolean result = backEndContainer.postMessageToLocalAgent(msg, pongAID);
		return result;
		
	}

	public String [] agentNames() {
		
		
		System.out.println("Executing Testing service HELPER (agentNames) ....");
		AID[] agentsAIDs = backEndContainer.agentNames();
		
		String [] agents = new String[agentsAIDs.length];
		
		for(int i = 0; i < agentsAIDs.length; i++){
			agents[i] = agentsAIDs[i].getName();
		}
		
		return agents;
	}

}
