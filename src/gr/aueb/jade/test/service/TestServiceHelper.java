package gr.aueb.jade.test.service;

import java.lang.reflect.Field;

import javax.swing.JFrame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.ServiceHelper;
import jade.domain.FIPANames;
import jade.imtp.leap.sms.PhoneBasedSMSManager;
import jade.imtp.leap.sms.SMSManager;
import jade.lang.acl.ACLMessage;
import jade.tools.rma.rma;

public class TestServiceHelper implements ServiceHelper {

	AgentContainer agentContainer;
	Agent myAgent;

	public TestServiceHelper(AgentContainer bec, Agent a) {
		this.agentContainer = bec;
		this.myAgent = a;
	}

	@Override
	public void init(Agent a) {
	}

	/**
	 * Sends a simple hello message to the FrontEndContainer
	 * @return true in case of success
	 */
	public String sendSmsMessage(){
		
		// get the singleton instance, should be already initialized
		SMSManager smsManager = SMSManager.getInstance(null);
		// and should be an instance of PhoneBasedSMSManager
		if (! (smsManager instanceof PhoneBasedSMSManager)){
			return "false";
		}
		
		smsManager.sendTextMessage("mymsisdn", 1100, "hello");
		
		return "true";
	}
	
	public String requestDropDown(){
		
		
		System.out.println("-----> Invoked requestDropDown.");
		return "false";
	}
	
	private String isRmaAgentVisible(String agentName) {

		rma agent = (rma) agentContainer.acquireLocalAgent(new AID(agentName,
				false));
		System.out.println(agentName + ":" + agent.getAgentState().getName());

		Field myGuiField;
		try {
			myGuiField = rma.class.getDeclaredField("myGUI");
			myGuiField.setAccessible(true);

			JFrame mainWindow = (JFrame) myGuiField.get(agent);

			if (mainWindow.isVisible()) {
				return Boolean.toString(true);
			}

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return Boolean.toString(false);
	}

	public String isRmaCloneVisible() {

		return isRmaAgentVisible("rmaClone");
	}
	
	public String isRmaVisible() {

		return isRmaAgentVisible("rma");
	}

	public String postMessageToLocalAgent() {

		System.out
				.println("Executing Testing service HELPER (postMessageToLocalAgent) ...."
						+ myAgent);

		AID pongAID = new AID("pong", false);

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.addReceiver(pongAID);
		msg.setContent("ping");

		boolean result = agentContainer.postMessageToLocalAgent(msg, pongAID);
		return Boolean.toString(result);

	}

	public String agentNames() {

		System.out
				.println("Executing Testing service HELPER (agentNames) ....");
		AID[] agentsAIDs = agentContainer.agentNames();

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < agentsAIDs.length; i++) {
			buffer.append(agentsAIDs[i].getName());
			buffer.append(";");
		}
		buffer.deleteCharAt(buffer.length() - 1);

		return buffer.toString();
	}

}
