package gr.aueb.jade.test.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public class ServiceInvokerBehaviour extends SimpleAchieveREInitiator {
	String methodName;
	ServiceInvocationCallback callback;
	AID backendCallerAID;
	
	public ServiceInvokerBehaviour(Agent a, String methodName, AID backendCallerAID, ServiceInvocationCallback callback) {
		super(a, null);
		this.methodName = methodName;
		this.callback = callback;
		this.backendCallerAID = backendCallerAID;
	}

	@Override
	protected ACLMessage prepareRequest(ACLMessage msg) {

		msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.addReceiver(backendCallerAID);
		msg.setContent(methodName);

		// System.out.println("Sending message" + msg.toString());
		return msg;
	}

	@Override
	protected void handleInform(ACLMessage msg) {

		callback.onMethodResult(methodName, msg.getContent());

	}

}

