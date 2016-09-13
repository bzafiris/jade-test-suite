package gr.aueb.jade.test.proto;

import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class MySubscriptionInitiator extends SubscriptionInitiator {

	public MySubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
	}
	
	@Override
	protected Vector prepareSubscriptions(ACLMessage subscription) {
		
		System.out.println("Subscription Initiator: Sending subscription ");
		
		return super.prepareSubscriptions(subscription);
	}
	
	@Override
	protected void handleAgree(ACLMessage agree) {

		System.out.println("Subscription Initiator: Received Agree");
	}
	

}
