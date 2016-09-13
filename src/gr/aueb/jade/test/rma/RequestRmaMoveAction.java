package gr.aueb.jade.test.rma;

import jade.content.AgentAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;

public class RequestRmaMoveAction extends RequestRmaAction {

	private static final long serialVersionUID = 4880697641125606392L;

	String destinationContainer;
	
	public RequestRmaMoveAction(Agent a, ACLMessage msg, AID ams,
			RequestOutcomeCallback callback, String destinationContainer) {
		super(a, msg, ams, callback);
		this.destinationContainer = destinationContainer;
	}

	@Override
	protected AgentAction getAction() {
		MoveAction moveAct = new MoveAction();
		MobileAgentDescription desc = new MobileAgentDescription();
		desc.setName(new AID("rma", false));

		ContainerID dest = new ContainerID(destinationContainer, null);
		desc.setDestination(dest);
		moveAct.setMobileAgentDescription(desc);

		System.out.println("Requesting move to " + destinationContainer);
		return moveAct;

	}

}
