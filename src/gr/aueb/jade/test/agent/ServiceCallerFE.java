package gr.aueb.jade.test.agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gr.aueb.jade.test.service.TestService;
import gr.aueb.jade.test.service.TestServiceFEHelper;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class ServiceCallerFE extends BaseAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -664958537352005212L;

	@Override
	protected void setup() {
		super.setup();

		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));

		FrontEndMethodInvocationResponder bmir = new FrontEndMethodInvocationResponder(this, mt);
		addBehaviour(bmir);

	}

	class FrontEndMethodInvocationResponder extends SimpleAchieveREResponder {

		public FrontEndMethodInvocationResponder(Agent a, MessageTemplate mt) {
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
				response.setContent("Could not initialize FrontEnd test service helper");
				return response;
			}

			String action = request.getContent();
			String result = null;
			Method serviceCall;
			
			try {
				serviceCall = testServiceHelper.getClass().getMethod(action, String.class);
				result = (String) serviceCall.invoke(testServiceHelper, getLocalName());
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				result = "Public method <String " + action + "(String actor)> is not available in " + TestServiceFEHelper.class.getName();
			}
 			response.setContent(result);

			return response;
		}

	}

}
