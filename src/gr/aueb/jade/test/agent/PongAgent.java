package gr.aueb.jade.test.agent;

import gr.aueb.jade.test.util.JadeUtility;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.SimpleAchieveREResponder;

/**
 * When activated sends a message to the pong message and waits for reply.
 * 
 * @author bzafiris
 *
 */
public class PongAgent extends BaseAgent {

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();

		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));

		PongBehaviour pb = new PongBehaviour(this, mt);
		addBehaviour(pb);

	}

	class PongBehaviour extends SimpleAchieveREResponder {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8618389684446341393L;

		public PongBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {

			System.out.println("Pong preparing response for: " + request);
			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);
			response.setContent("pong");
			return response;

		}

	}

}
