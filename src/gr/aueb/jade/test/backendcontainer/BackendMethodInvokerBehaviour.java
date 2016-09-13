package gr.aueb.jade.test.backendcontainer;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

class BackendMethodInvokerBehaviour extends SimpleAchieveREInitiator {
	String methodName;
	BackendMethodCallback callback;
	AID backendCallerAID;
	
	public BackendMethodInvokerBehaviour(Agent a, String methodName, AID backendCallerAID, BackendMethodCallback callback) {
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

		return msg;
	}

	@Override
	protected void handleInform(ACLMessage msg) {

		callback.onMethodResult(methodName, msg.getContent());

	}

}

