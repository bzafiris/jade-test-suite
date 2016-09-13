package gr.aueb.jade.test.backendcontainer;

import gr.aueb.jade.test.agent.BaseAgent;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class BackendServiceCaller extends BaseAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -664958537352005212L;

	@Override
	protected void setup() {
		super.setup();

		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));

		BackendMethodInvocationResponder bmir = new BackendMethodInvocationResponder(this, mt);
		addBehaviour(bmir);

	}

	class AssertionBehaviour extends OneShotBehaviour {

		@Override
		public void action() {

			System.out.println("Executing one shot behaviour");

			try {
				TestServiceFEHelper testServiceHelper = (TestServiceFEHelper) getHelper(TestService.class.getName());
				String[] agents = new String[] {};
				agents = testServiceHelper.getAgentNames(getLocalName());

				System.out.println("----> printing agent names...");
				for (String aid : agents) {
					System.out.println(aid);
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class BackendMethodInvocationResponder extends SimpleAchieveREResponder {

		public BackendMethodInvocationResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);

			TestServiceFEHelper testServiceHelper;
			try {
				testServiceHelper = (TestServiceFEHelper) getHelper(TestService.class.getName());
			} catch (ServiceException e1) {
				response.setPerformative(ACLMessage.FAILURE);
				e1.printStackTrace();
				response.setContent("Could not initialize test service helper");
				return response;
			}

			String action = request.getContent();

			if (action.equals("agentNames")) {
				String[] agents = new String[] {};
				agents = testServiceHelper.getAgentNames(getLocalName());

				// System.out.println("----> Preparing agent names...");
				StringBuffer buffer = new StringBuffer();

				for (String aid : agents) {
					buffer.append(aid);
					buffer.append(";");
				}
				buffer.deleteCharAt(buffer.length() - 1);

				response.setContent(buffer.toString());
				
			} else if (action.equals("postMessageToLocalAgent")){
				boolean result = testServiceHelper.postMessageToLocalAgent(getLocalName());
				response.setContent(Boolean.toString(result));
			}

			return response;
		}

	}

}
