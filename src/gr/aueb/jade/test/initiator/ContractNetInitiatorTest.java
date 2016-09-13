package gr.aueb.jade.test.initiator;

import gr.aueb.jade.test.proto.MyContractNetInitiator;
import gr.aueb.jade.test.responder.ResponderAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import test.common.Test;
import test.common.TestException;

public class ContractNetInitiatorTest extends Test {

	// reset on handleInform calls reinit and restarts behaviour
	boolean onReset_ContractNetRestarts = false;
	private ACLMessage cfpMsg;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		cfpMsg = new ACLMessage(ACLMessage.CFP);
		cfpMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		cfpMsg.setContent(ResponderAgent.CONTRACT_NET_REQUEST);
		cfpMsg.addReceiver(recipient);

		MyContractNetInitiator initiator1 = new MyContractNetInitiator(a, cfpMsg) {

			int round = 1;

			@Override
			protected void handleInform(ACLMessage inform) {
				
				super.handleInform(inform);

				if (round == 1) {
					round++;
					reset(cfpMsg);
				} else if (round == 2) {
					onReset_ContractNetRestarts = true;
				}

			}
		};

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(initiator1);
		sb.addSubBehaviour(new AssertionBehavior());
		return sb;

	}

	class AssertionBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			if (onReset_ContractNetRestarts) {
				passed("Responder tests passed!");
			} else {
				failed("Responder tests failed!");
			}
		}

	}

}
