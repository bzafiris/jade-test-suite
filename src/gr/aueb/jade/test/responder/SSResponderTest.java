package gr.aueb.jade.test.responder;

import java.util.Date;
import java.util.Vector;

import gr.aueb.jade.test.agent.DelayBehaviour;
import gr.aueb.jade.test.proto.MyContractNetInitiator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.TwoPhInitiator;
import test.common.Test;
import test.common.TestException;

public class SSResponderTest extends Test  {

	// ds must contain no messages after succesful contract net execution
	boolean nextReplySenderDeadlineActivatedAndViolated = false;
	private SequentialBehaviour checkStatusAndAssertionBehaviour;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID recipient = new AID("responderAgent", false);

		
		
		ACLMessage msg1 = new ACLMessage(ACLMessage.CFP);
		msg1.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		msg1.setContent(ResponderAgent.CONTRACT_NET_REQUEST);
		msg1.addReceiver(recipient);

		ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
		msg2.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg2.setContent(ResponderAgent.CHECK_CONTRACT_NET_RESPONDER_DS);
		msg2.addReceiver(recipient);

		MyContractNetInitiator initiator1 = new MyContractNetInitiator(a, msg1) {
			@Override
			protected void handlePropose(ACLMessage propose, Vector acceptances) {
				try {
					System.out.println("Contract Net Initiator delaying accept-proposal for 5 sec");
					// delay in order to cause expiration of the MsgReceiver of the ContractNetResponder
					// an inform message will never be received
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.handlePropose(propose, acceptances);
				
				((ParallelBehaviour)getParent()).addSubBehaviour(checkStatusAndAssertionBehaviour);
			}
		};
		

		CheckStatusInitiator initiator2 = new CheckStatusInitiator(a, msg2);

		DelayBehaviour db = new DelayBehaviour(5000);

		ParallelBehaviour pb = new ParallelBehaviour();
		pb.addSubBehaviour(initiator1);
		
		checkStatusAndAssertionBehaviour = new SequentialBehaviour();
		checkStatusAndAssertionBehaviour.addSubBehaviour(db);
		checkStatusAndAssertionBehaviour.addSubBehaviour(initiator2);
		checkStatusAndAssertionBehaviour.addSubBehaviour(new AssertionBehavior());
		
		return pb;

	}

	class AssertionBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			if (nextReplySenderDeadlineActivatedAndViolated) {
				passed("ContractNetResponder tests passed!");
			} else {
				failed("ContractNetResponder tests failed!");
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

			if (parts[0].equals(ResponderAgent.CHECK_CONTRACT_NET_RESPONDER_DS)) {
				if (parts[1].equals("true")) {
					nextReplySenderDeadlineActivatedAndViolated = true;
				}
			}
			System.out.println("Received Inform:" + inform.getContent());

		}

	}
}
