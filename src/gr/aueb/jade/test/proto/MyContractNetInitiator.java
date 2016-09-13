package gr.aueb.jade.test.proto;

import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.TwoPhInitiator;

public class MyContractNetInitiator extends ContractNetInitiator {

	public MyContractNetInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {

		System.out.println("Contract Net Initiator: Received propose " );

		ACLMessage acceptProposal = propose.createReply();
		acceptProposal.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		
		acceptances.add(acceptProposal);
		System.out.println("Contract Net Initiator: Sending accept proposal ");
	}
	
	@Override
	protected void handleAllResultNotifications(Vector resultNotifications) {

		ACLMessage inform = (ACLMessage) resultNotifications.get(0);
		System.out.println("Contract Net Initiator: Received inform from " + inform.getSender());
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Contract Net Initiator: Received inform from " + inform.getSender());
	}

}