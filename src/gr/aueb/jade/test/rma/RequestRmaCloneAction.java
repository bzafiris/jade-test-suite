package gr.aueb.jade.test.rma;

import jade.content.AgentAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.lang.acl.ACLMessage;

public class RequestRmaCloneAction extends RequestRmaAction {

	String destinationContainer;
	
	public RequestRmaCloneAction(Agent a, ACLMessage msg, AID ams, 
			RequestOutcomeCallback callback, String destinationContainer) {
		super(a, msg, ams, callback);
		this.destinationContainer = destinationContainer;
	}

	@Override
	protected AgentAction getAction() {
		CloneAction cloneAct = new CloneAction();
		MobileAgentDescription desc = new MobileAgentDescription();
		desc.setName(new AID("rma", false));

		ContainerID dest = new ContainerID(destinationContainer, null);
		desc.setDestination(dest);
		cloneAct.setMobileAgentDescription(desc);
		cloneAct.setNewName("rmaClone");
		return cloneAct;
	}

}
