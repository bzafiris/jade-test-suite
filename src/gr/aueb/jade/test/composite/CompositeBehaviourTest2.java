package gr.aueb.jade.test.composite;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.SimpleAchieveREInitiator;
import test.common.Test;
import test.common.TestException;

public class CompositeBehaviourTest2 extends Test {

	
	CheckBlockedBehaviourInitiator checkBlockedBehaviourInitiator;
	CheckBlockedBehaviourInitiator checkBlockedBehaviourInitiator2;
	
	@Override
	public Behaviour load(final Agent a) throws TestException {
		
		checkBlockedBehaviourInitiator = new CheckBlockedBehaviourInitiator(a);
		RestartBehaviourInitiator rbi = new RestartBehaviourInitiator(a, null);
		checkBlockedBehaviourInitiator2 = new CheckBlockedBehaviourInitiator(a);
		
		AssertBehaviour ab = new AssertBehaviour();
		
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(checkBlockedBehaviourInitiator);
		sb.addSubBehaviour(rbi);
		sb.addSubBehaviour(checkBlockedBehaviourInitiator2);
		sb.addSubBehaviour(ab);
		return sb;
	};
	
	class AssertBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			
			// the behaviour must be initially blocked and then unblocked
			if (checkBlockedBehaviourInitiator.getCheckResult().equals("true") &&
					checkBlockedBehaviourInitiator2.getCheckResult().equals("false")){
				passed("Test passed");
			} else {
				failed("Restart behaviour test failed");
			}
		}
	}
	
	class RestartBehaviourInitiator extends SimpleAchieveREInitiator {

		public RestartBehaviourInitiator(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected ACLMessage prepareRequest(ACLMessage msg) {
			
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.addReceiver(new AID("compositeAgent2", false));
			msg.setContent("restart");

			return msg;
		}
		
		@Override
		protected void handleInform(ACLMessage msg) {
			if (!msg.getContent().equals("ok")){
				failed("Restart failed");
			}
		}
		
	}

	class CheckBlockedBehaviourInitiator extends SimpleAchieveREInitiator {

		String checkResult;
		
		public CheckBlockedBehaviourInitiator(Agent a) {
			super(a, null);
		}
		
		@Override
		protected ACLMessage prepareRequest(ACLMessage msg) {

			// wait for compositeAgent to execute and block
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.addReceiver(new AID("compositeAgent2", false));
			msg.setContent("isBlocked");

			return msg;
		}
		
		@Override
		protected void handleInform(ACLMessage msg) {
			checkResult = msg.getContent();
		}
		
		@Override
		protected void handleFailure(ACLMessage msg) {
			failed("CompositeBehaviour block test failed.");
		}
		
		public String getCheckResult() {
			return checkResult;
		}
		
	}
	
	

}
