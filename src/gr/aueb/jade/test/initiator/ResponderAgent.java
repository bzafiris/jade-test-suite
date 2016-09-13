package gr.aueb.jade.test.initiator;

import java.util.Date;

import gr.aueb.jade.test.agent.BaseAgent;
import gr.aueb.jade.test.proto.MyContractNetResponder;
import gr.aueb.jade.test.proto.MySubscriptionResponder;
import gr.aueb.jade.test.proto.MyTwoPhResponder;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.MyNextMsgReceiver;
import jade.proto.SubscriptionResponder;
import jade.util.leap.Set;

public class ResponderAgent extends BaseAgent {

	
	public static final String TEST_FIPA_REQUEST_RESTART = "testFipaRequestRestart";
	public static final String SUBSCRIBE_REQUEST = "subscribeRequest";
	
	public static final String CONTRACT_NET_REQUEST = "contractNetRequest";

	MyAchieveREResponder informResponder;
	MyContractNetResponder contractNetResponder;
	MySubscriptionResponder subscriptionResponder;
	
	@Override
	protected void setup() {
		super.setup();

		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
				MessageTemplate.MatchContent(TEST_FIPA_REQUEST_RESTART));

		MessageTemplate mt2 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP)),
				MessageTemplate.MatchContent(CONTRACT_NET_REQUEST));
		
		MessageTemplate mt3 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE),
						MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE)),
				MessageTemplate.MatchContent(SUBSCRIBE_REQUEST));

		informResponder = new MyAchieveREResponder(this, mt);
		
		contractNetResponder = new MyContractNetResponder(this, mt2);
		
		subscriptionResponder = new MySubscriptionResponder(this, mt3);
		
		addBehaviour(informResponder);
		addBehaviour(contractNetResponder);
		addBehaviour(subscriptionResponder);

	}

	private String serializeKeyset(Set keys) {

		Object[] objKeyArray = keys.toArray();
		String[] keyArray = new String[objKeyArray.length];

		for (int i = 0; i < objKeyArray.length; i++) {
			keyArray[i] = (String) objKeyArray[i];
		}

		return String.join("|", keyArray);

	}

	
	class MyAchieveREResponder extends AchieveREResponder {

		public MyAchieveREResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);
			response.setContent(TEST_FIPA_REQUEST_RESTART + "|" + serializeKeyset(getDataStore().keySet()));
			return response;
		}

	}

	
}
