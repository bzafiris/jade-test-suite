package gr.aueb.jade.test.proto;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.TwoPhResponder;

public class MyTwoPhResponder extends TwoPhResponder {

	public MyTwoPhResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) {

		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		propose.setContent("proposal");

		return propose;
	}

	@Override
	protected ACLMessage handleQueryIf(ACLMessage queryIf) {

		ACLMessage confirm = queryIf.createReply();
		confirm.setPerformative(ACLMessage.CONFIRM);
		confirm.setContent("proposal");

		return confirm;
	}

	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage accept) {

		ACLMessage result = accept.createReply();
		result.setPerformative(ACLMessage.INFORM);
		result.setContent("ready");

		return result;
	}

}