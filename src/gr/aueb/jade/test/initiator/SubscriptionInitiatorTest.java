package gr.aueb.jade.test.initiator;

import gr.aueb.jade.test.proto.MyContractNetInitiator;
import gr.aueb.jade.test.proto.MySubscriptionInitiator;
import gr.aueb.jade.test.initiator.ResponderAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import test.common.Test;
import test.common.TestException;

public class SubscriptionInitiatorTest extends Test {

	// reset on handleInform calls reinit and restarts behaviour
	boolean onReset_FipaSubscribeRestarts = false;
	private ACLMessage cfpMsg;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		cfpMsg = new ACLMessage(ACLMessage.SUBSCRIBE);
		cfpMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		cfpMsg.setContent(ResponderAgent.SUBSCRIBE_REQUEST);
		cfpMsg.addReceiver(recipient);

		MySubscriptionInitiator initiator1 = new MySubscriptionInitiator(a, cfpMsg) {

			int round = 1;

			@Override
			protected void handleAgree(ACLMessage inform) {
				
				super.handleAgree(inform);

				if (round == 1) {
					round++;
					reset(cfpMsg);
				} else if (round == 2) {
					onReset_FipaSubscribeRestarts = true;
					
					passed("Test passed!");
				
				}

			}
			
			
		};

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(initiator1);
		return sb;

	}

	
}
