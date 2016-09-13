package gr.aueb.jade.test.composite;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import test.common.Test;
import test.common.TestException;

public class CompositeBehaviourTest extends Test {

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";

	AID backendCallerAID = null;

	class CheckBlockedBehaviourInitiator extends SimpleAchieveREInitiator {

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
			msg.addReceiver(new AID("compositeAgent", false));
			msg.setContent("isBlocked");

			return msg;
		}
		
		@Override
		protected void handleInform(ACLMessage msg) {
			
			String result = msg.getContent();
			System.out.println("Result:" + result);
			if (result.equals("true")){
				passed("Test passed");
			} else {
				failed("CompositeBehaviour block test failed.");
			}
			
		}
		
		@Override
		protected void handleFailure(ACLMessage msg) {
			failed("CompositeBehaviour block test failed.");
		}
		
	}
	
	@Override
	public Behaviour load(final Agent a) throws TestException {
		
		return new CheckBlockedBehaviourInitiator(a);
	};

}
