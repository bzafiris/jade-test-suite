package gr.aueb.jade.test.rma;

import gr.aueb.jade.test.util.JadeUtility;
import jade.content.AgentAction;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public abstract class RequestRmaAction extends SimpleAchieveREInitiator {

	AID ams;
	String informResult;
	String failureResult;
	RequestOutcomeCallback callback;

	public RequestRmaAction(Agent a, ACLMessage msg, AID ams,
			RequestOutcomeCallback callback) {
		super(a, msg);
		this.ams = ams;
		this.callback = callback;
	}

	@Override
	protected ACLMessage prepareRequest(ACLMessage msg) {

		AgentAction agentAction = getAction();

		try {
			Action a = new Action();
			a.setActor(ams);
			a.setAction(agentAction);

			ACLMessage requestMsg = getACLRequest();
			myAgent.getContentManager().fillContent(requestMsg, a);

			return requestMsg;
		} catch (Exception fe) {
			fe.printStackTrace();
		}

		return null;
	}

	/**
	 * Override this method to create a custom acl request. This version creates
	 * a request that is sent to the AMS agent and uses the MobilityOntology
	 * 
	 * @return
	 */
	protected ACLMessage getACLRequest() {
		ACLMessage requestMsg = JadeUtility.getBasicRequest(myAgent, ams);
		requestMsg.setOntology(MobilityOntology.NAME);
		return requestMsg;
	}

	protected abstract AgentAction getAction();

	@Override
	protected void handleInform(ACLMessage msg) {
		informResult = msg.getContent();
		callback.onInform(informResult);
	}

	@Override
	protected void handleFailure(ACLMessage msg) {
		failureResult = msg.getContent();
		callback.onFailure(failureResult);
	}

	public String getFailureResult() {
		return failureResult;
	}

	public String getInformResult() {
		return informResult;
	}

	public boolean isSuccessful() {
		return failureResult == null;
	}

};
