package gr.aueb.jade.test.proto;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

public class MySubscriptionResponder extends SubscriptionResponder {

	public MySubscriptionResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException, RefuseException {
		
		ACLMessage accept = subscription.createReply();
		accept.setPerformative(ACLMessage.AGREE);
		
		System.out.println("Subscription Responder: Sending Agree");
		
		return accept;
		
	}
	
	

}
