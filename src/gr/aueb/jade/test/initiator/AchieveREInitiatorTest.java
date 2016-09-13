package gr.aueb.jade.test.initiator;

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

public class AchieveREInitiatorTest extends Test {

	// reset on handleInform calls reinit and restarts behaviour
	boolean onReset_FipaRequestRestarts = false;
	private ACLMessage initiatorMsg;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		initiatorMsg = new ACLMessage(ACLMessage.REQUEST);
		initiatorMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		initiatorMsg.setContent(ResponderAgent.TEST_FIPA_REQUEST_RESTART);
		initiatorMsg.addReceiver(recipient);

		MessageInitiator initiator1 = new MessageInitiator(a, initiatorMsg);

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(initiator1);
		sb.addSubBehaviour(new AssertionBehavior());
		return sb;

	}

	class AssertionBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			if (onReset_FipaRequestRestarts) {
				passed("Responder tests passed!");
			} else {
				failed("Responder tests failed!");
			}
		}

	}

	class MessageInitiator extends AchieveREInitiator {

		int round = 1;

		public MessageInitiator(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleInform(ACLMessage inform) {

			if (round == 1) {
				round++;
				reset(initiatorMsg);
			} else if (round == 2) {
				onReset_FipaRequestRestarts = true;
			}
			
			System.out.println("Received Inform:" + inform.getContent());

		}

	}
}
