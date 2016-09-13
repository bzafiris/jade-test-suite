package gr.aueb.jade.test.agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gr.aueb.jade.test.backendcontainer.TestServiceFEHelper;
import gr.aueb.jade.test.service.TestService;
import gr.aueb.jade.test.service.TestServiceHelper;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class ServiceCaller extends BaseAgent {

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

	class BackendMethodInvocationResponder extends SimpleAchieveREResponder {

		public BackendMethodInvocationResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {

			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.INFORM);

			TestServiceHelper testServiceHelper;
			try {
				testServiceHelper = (TestServiceHelper) getHelper(TestService.class.getName());
			} catch (ServiceException e1) {
				response.setPerformative(ACLMessage.FAILURE);
				e1.printStackTrace();
				response.setContent("Could not initialize test service helper");
				return response;
			}

			String action = request.getContent();
			String result = null;
			Method serviceCall;
			
			try {
				serviceCall = testServiceHelper.getClass().getMethod(action, null);
				result = (String) serviceCall.invoke(testServiceHelper, new Object[]{});
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				result = "Public method <String " + action + "()> is not available in " + TestServiceHelper.class.getName();
			}
			
			response.setContent(result);

			return response;
		}

	}

}
