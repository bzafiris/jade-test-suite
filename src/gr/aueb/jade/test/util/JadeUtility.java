package gr.aueb.jade.test.util;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.IntrospectionOntology;
import jade.lang.acl.ACLMessage;

public class JadeUtility {

	public static AID getAIDByLocalName(Agent a, String sLocalName) {
		try {
			AMSAgentDescription amsDescr = new AMSAgentDescription();
			AMSAgentDescription[] res = null;
			amsDescr.setName(new AID(sLocalName, false));
			res = AMSService.search(a, amsDescr);
			if (res.length == 0)
				return null;
			return res[0].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
	}
	
	public static AID getAMS(Agent a) {
		return getAIDByLocalName(a, "ams");
	}

	/**
	 * Create a request that will be attributed to the AMS platform manager agent
	 * @param a
	 * @return
	 */
	public static ACLMessage getBasicRequest(Agent a, AID receiver){
		
		ACLMessage AMSRequest = new ACLMessage(ACLMessage.REQUEST);
		AMSRequest.setSender(a.getAID());
		AMSRequest.addReceiver(receiver);
		AMSRequest.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		AMSRequest.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		return AMSRequest;
		
	}
	
	public static ACLMessage getAMSSubscriptionMessage(Agent a){
		
		// Fill ACL messages fields
		ACLMessage AMSSubscription = new ACLMessage(ACLMessage.SUBSCRIBE);
		AMSSubscription.setSender(a.getAID());
		AMSSubscription.clearAllReceiver();
		AMSSubscription.addReceiver(getAMS(a));
		AMSSubscription.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		AMSSubscription.setOntology(IntrospectionOntology.NAME);
		AMSSubscription.setReplyWith(AMSSubscriber.AMS_SUBSCRIPTION);
		AMSSubscription.setConversationId(a.getLocalName());
		AMSSubscription.setContent(AMSSubscriber.PLATFORM_EVENTS);
		return AMSSubscription;
		
	}
	
}
