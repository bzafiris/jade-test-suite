package gr.aueb.jade.test.responder;

import gr.aueb.jade.test.agent.DelayBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import test.common.Test;
import test.common.TestException;

public class AchieveREResponderTest extends Test {

	// ds must contain the request message
	boolean onInformResponse_DsContains_1message = false;
	// ds must contain the request message
	boolean onAgreeResponse_DsContains_1message = false;
	// ds must contain the request message and response message sent onAgree
	boolean onAgreeResponseAndInformResult_DsContains_2messages = false;
	// ds is empty once AchieveREResponder sends the inform message
	boolean onInformReceived_DsContains_NoMessages = false;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		ACLMessage msg1 = new ACLMessage(ACLMessage.REQUEST);
		msg1.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg1.setContent(ResponderAgent.CHECK_INFORM_DS);
		msg1.addReceiver(recipient);

		ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
		msg2.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg2.setContent(ResponderAgent.CHECK_AGREE_AND_INFORM_DS);
		msg2.addReceiver(recipient);

		ACLMessage msg3 = new ACLMessage(ACLMessage.REQUEST);
		msg3.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg3.setContent(ResponderAgent.CHECK_BOTH_DS);
		msg3.addReceiver(recipient);

		MessageInitiator initiator1 = new MessageInitiator(a, msg1);
		MessageInitiator initiator2 = new MessageInitiator(a, msg2);
		MessageInitiator initiator3 = new MessageInitiator(a, msg3);

		DelayBehaviour db = new DelayBehaviour(1000);

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(initiator1);
		sb.addSubBehaviour(db);
		sb.addSubBehaviour(initiator2);
		sb.addSubBehaviour(db);
		sb.addSubBehaviour(initiator3);
		sb.addSubBehaviour(new AssertionBehavior());
		return sb;

	}

	class AssertionBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			if (onAgreeResponse_DsContains_1message && onAgreeResponseAndInformResult_DsContains_2messages
					&& onInformReceived_DsContains_NoMessages && onInformResponse_DsContains_1message) {
				passed("Responder tests passed!");
			} else {
				failed("Responder tests failed!");
			}
		}

	}

	class MessageInitiator extends AchieveREInitiator {

		public MessageInitiator(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleAgree(ACLMessage agree) {

			String content = agree.getContent();
			String[] parts = content.split("\\|");

			if (parts.length == 2 && parts[0].equals(ResponderAgent.CHECK_AGREE_AND_INFORM_DS)) {
				onAgreeResponse_DsContains_1message = true;
			}
			System.out.println("Received Agree:" + content);

		}

		@Override
		protected void handleInform(ACLMessage inform) {
			String content = inform.getContent();
			String[] parts = content.split("\\|");

			if (parts.length <= 1) {
				return;
			}

			if (parts[0].equals(ResponderAgent.CHECK_INFORM_DS)) {
				if (parts.length == 2) {
					onInformResponse_DsContains_1message = true;
				}
			} else if (parts[0].equals(ResponderAgent.CHECK_AGREE_AND_INFORM_DS)) {
				if (parts.length == 3) {
					onAgreeResponseAndInformResult_DsContains_2messages = true;
				}

			} else if (parts[0].equals(ResponderAgent.CHECK_BOTH_DS)) {
				if (parts[2].equals("0") && parts[4].equals("0")) {
					onInformReceived_DsContains_NoMessages = true;
				}
			}
			System.out.println("Received Inform:" + inform.getContent());

		}

	}
}
