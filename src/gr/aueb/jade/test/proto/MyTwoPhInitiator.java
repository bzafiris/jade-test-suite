package gr.aueb.jade.test.proto;

import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.TwoPhInitiator;

public class MyTwoPhInitiator extends TwoPhInitiator {

	public MyTwoPhInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handleAllPh0Responses(Vector responses, Vector proposes, Vector pendings, Vector nextPhMsgs) {

		if (proposes.size() == 1) {
			System.out
					.println("Ph0 Initiator: Received propose from " + ((ACLMessage) proposes.get(0)).getSender());
		}

		if (nextPhMsgs.size() == 1) {
			System.out.println(
					"Ph0 Initiator: Sending messages of type  " + ((ACLMessage) nextPhMsgs.get(0)).toString());
		}

		super.handleAllPh0Responses(responses, proposes, pendings, nextPhMsgs);
	}

	@Override
	protected void handleAllPh1Responses(Vector responses, Vector confirms, Vector disconfirms, Vector informs,
			Vector pendings, Vector nextPhMsgs) {

		if (confirms.size() == 1) {
			System.out.println(
					"Ph1 Initiator: Received confirmation from " + ((ACLMessage) confirms.get(0)).getSender());
		}

		if (nextPhMsgs.size() == 1) {
			System.out.println(
					"Ph1 Initiator: Sending messages of type  " + ((ACLMessage) nextPhMsgs.get(0)).toString());
		}

		super.handleAllPh1Responses(responses, confirms, disconfirms, informs, pendings, nextPhMsgs);
	}

	@Override
	protected void handlePh2Inform(ACLMessage inform) {

		System.out.println("Ph2 Initiator: Received inform from " + inform.getSender());

		super.handlePh1Inform(inform);
	}

}