package gr.aueb.jade.test.agent;

import java.util.Iterator;

import gr.aueb.jade.test.util.JadeUtility;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SimpleAchieveREInitiator;

/**
 * When activated sends a message to the pong message and waits for reply.
 * 
 * @author bzafiris
 *
 */
@Deprecated
class PingAgent extends BaseAgent {

	AID pongAID = null;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();

		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));

		ActivatePingBehaviour apb = new ActivatePingBehaviour(this, mt);
		apb.registerHandleRequest(new PingBehaviour(this));
		addBehaviour(apb);

	}

	class ActivatePingBehaviour extends AchieveREResponder {

		public ActivatePingBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

	}

	class PingBehaviour extends SimpleAchieveREInitiator {

		public PingBehaviour(Agent a) {
			super(a, null);
		}

		@Override
		protected ACLMessage prepareRequest(ACLMessage msg) {

			//ACLMessage parentRequest = (ACLMessage) getParent().getDataStore().get(REQUEST_KEY);
			printDatastore();
			//System.out.println(parentRequest.toString());
			
			if (pongAID == null) {
				pongAID = JadeUtility.getAIDByLocalName(myAgent, "pong");
			}
			
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.addReceiver(pongAID);
			msg.setContent("ping");

			return msg;
		}

		private void printDatastore() {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("Printing datastore!!!");
			DataStore ds = getDataStore();
			Iterator it = ds.keySet().iterator();
			while(it.hasNext()){
				Object key = it.next();
				System.out.println("Key " + key + "----> " + ds.get(key));
			}
		}

		@Override
		protected void handleInform(ACLMessage msg) {

			System.out.println("Pong !!!! (" + msg.getContent() + ")");
			printDatastore();
		}

	}

}
