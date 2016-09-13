package gr.aueb.jade.test.responder;

import gr.aueb.jade.test.agent.DelayBehaviour;
import gr.aueb.jade.test.proto.MyTwoPhInitiator;
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

public class ResponderTest extends Test {

	// ds must contain no messages after succesful contract net execution
	boolean onTwoPhResponderInform_DsContains_NoMessages = false;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		ACLMessage msg1 = new ACLMessage(ACLMessage.CFP);
		msg1.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		msg1.setContent(ResponderAgent.TWO_PH_REQUEST);
		msg1.addReceiver(recipient);
		
		ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
		msg2.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg2.setContent(ResponderAgent.CHECK_TWO_PH_RESPONDER_DS);
		msg2.addReceiver(recipient);

		MyTwoPhInitiator initiator1 = new MyTwoPhInitiator(a, msg1);
		
		CheckStatusInitiator initiator2 = new CheckStatusInitiator(a, msg2);
		
		DelayBehaviour db = new DelayBehaviour(1000);

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(initiator1);
		sb.addSubBehaviour(db);
		sb.addSubBehaviour(initiator2);
		sb.addSubBehaviour(new AssertionBehavior());
		return sb;

	}

	class AssertionBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			if (onTwoPhResponderInform_DsContains_NoMessages) {
				passed("TwoPhResponder tests passed!");
			} else {
				failed("TwoPhResponder tests failed!");
			}
		}

	}

	class CheckStatusInitiator extends AchieveREInitiator {

		public CheckStatusInitiator(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			String content = inform.getContent();
			String[] parts = content.split("\\|");

			if (parts.length <= 1) {
				return;
			}

			if (parts[0].equals(ResponderAgent.CHECK_TWO_PH_RESPONDER_DS)) {
				if (parts[1].equals("0")) {
					onTwoPhResponderInform_DsContains_NoMessages = true;
				}
			}
			System.out.println("Received Inform:" + inform.getContent());

		}

	}
}
