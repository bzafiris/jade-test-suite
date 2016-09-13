package gr.aueb.jade.test.responder;

import java.util.Date;

import gr.aueb.jade.test.agent.BaseAgent;
import gr.aueb.jade.test.proto.MyContractNetResponder;
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
import jade.util.leap.Set;

public class ResponderAgent extends BaseAgent {

	public static final String CHECK_TWO_PH_RESPONDER_DS = "checkTwoPhResponderDs";
	public static final String CHECK_CONTRACT_NET_RESPONDER_DS = "checkContractNetResponderDs";
	public static final String CHECK_BOTH_DS = "status";
	public static final String CHECK_AGREE_AND_INFORM_DS = "testAgreeAndInformResult";
	public static final String CHECK_INFORM_DS = "checkAchieveREResponderDs";
	public static final String CONTRACT_NET_REQUEST = "contractNetRequest";
	public static final String TWO_PH_REQUEST = "twoPhRequest";

	InformResponder informResponder;
	AgreeAndInformResponder agreeAndInformResponder;
	MyTwoPhResponder twoPhResponder;
	MyContractNetResponder contractNetResponder;
	MyNextMsgReceiver msgReceiver;
	
	@Override
	protected void setup() {
		super.setup();

		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
				MessageTemplate.MatchContent(CHECK_INFORM_DS));

		MessageTemplate mt1 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
				MessageTemplate.MatchContent(CHECK_AGREE_AND_INFORM_DS));

		MessageTemplate mt2 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
				MessageTemplate.or(MessageTemplate.MatchContent(CHECK_BOTH_DS),
						MessageTemplate.or(MessageTemplate.MatchContent(CHECK_TWO_PH_RESPONDER_DS),
								MessageTemplate.MatchContent(CHECK_CONTRACT_NET_RESPONDER_DS))));

		MessageTemplate mt3 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP)),
				MessageTemplate.MatchContent(TWO_PH_REQUEST));

		MessageTemplate mt4 = MessageTemplate.and(
				MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP)),
				MessageTemplate.MatchContent(CONTRACT_NET_REQUEST));

		informResponder = new InformResponder(this, mt);
		agreeAndInformResponder = new AgreeAndInformResponder(this, mt1);

		twoPhResponder = new MyTwoPhResponder(this, mt3);
		contractNetResponder = new MyContractNetResponder(this, mt4) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) {
				ACLMessage propose = super.handleCfp(cfp);
				// set deadline for next message after 3 seconds
				Date date = new Date();
				date.setSeconds(date.getSeconds() + 3);
				propose.setReplyByDate(date);
				return propose;
			}
		};
		
		
		msgReceiver = new MyNextMsgReceiver(this, contractNetResponder);
		msgReceiver.registerWithSSResponder();

		addBehaviour(informResponder);
		addBehaviour(agreeAndInformResponder);
		addBehaviour(new StatusResponder(this, mt2));
		addBehaviour(twoPhResponder);
		addBehaviour(contractNetResponder);

	}

	private String serializeKeyset(Set keys) {

		Object[] objKeyArray = keys.toArray();
		String[] keyArray = new String[objKeyArray.length];

		for (int i = 0; i < objKeyArray.length; i++) {
			keyArray[i] = (String) objKeyArray[i];
		}

		return String.join("|", keyArray);

	}

	class StatusResponder extends AchieveREResponder {

		public StatusResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);

			String requestContent = request.getContent();

			String content = null;

			if (requestContent.equals(CHECK_TWO_PH_RESPONDER_DS)) {
				content = String.join("|", CHECK_TWO_PH_RESPONDER_DS,
						Integer.toString(twoPhResponder.getDataStore().keySet().size()));
			} else if (requestContent.equals(CHECK_CONTRACT_NET_RESPONDER_DS)) {
				content = String.join("|", CHECK_CONTRACT_NET_RESPONDER_DS,
						Boolean.toString(msgReceiver.deadlineExpired));
			} else if (requestContent.equals(CHECK_BOTH_DS)) {
				content = String.join("|", CHECK_BOTH_DS, CHECK_INFORM_DS,
						Integer.toString(informResponder.getDataStore().keySet().size()), CHECK_AGREE_AND_INFORM_DS,
						Integer.toString(agreeAndInformResponder.getDataStore().keySet().size()));
			}

			response.setContent(content);

			return response;
		}

	}

	class InformResponder extends AchieveREResponder {

		public InformResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);
			response.setContent(CHECK_INFORM_DS + "|" + serializeKeyset(getDataStore().keySet()));
			return response;
		}

	}

	class AgreeAndInformResponder extends AchieveREResponder {

		public AgreeAndInformResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.AGREE);
			response.setContent(CHECK_AGREE_AND_INFORM_DS + "|" + serializeKeyset(getDataStore().keySet()));

			return response;
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
				throws FailureException {

			ACLMessage result = request.createReply();
			result.setPerformative(ACLMessage.INFORM);
			result.setContent(CHECK_AGREE_AND_INFORM_DS + "|" + serializeKeyset(getDataStore().keySet()));

			return result;
		}

	}

}
