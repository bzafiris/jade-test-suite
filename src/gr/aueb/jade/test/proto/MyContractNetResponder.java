package gr.aueb.jade.test.proto;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class MyContractNetResponder extends ContractNetResponder {

	public MyContractNetResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) {

		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		propose.setContent("proposal");
		System.out.println("Contract Net responder: Received cfp");
		return propose;
	}
	
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {

		System.out.println("Contract Net responder: Received accept proposal");
		ACLMessage result = accept.createReply();
		result.setPerformative(ACLMessage.INFORM);
		result.setContent("ready");

		return result;
	}
	
	
}