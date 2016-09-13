package gr.aueb.jade.test.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gr.aueb.jade.test.agent.BaseAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CompositeBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import jade.util.leap.Collection;

public class CompositeBehaviourAgent extends BaseAgent {

	@Override
	protected void setup() {
		super.setup();

		MyCompositeBehaviour mcb = new MyCompositeBehaviour();
		for (int i = 0; i < 10; i++) {
			mcb.addSubBehaviour(new CountIncreaseBehaviour(mcb));
		}

		addBehaviour(mcb);

		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));

		CompositeBehaviourCheckResponder cbcr = new CompositeBehaviourCheckResponder(this, mt, mcb);

		addBehaviour(cbcr);
		
	}

	class CountIncreaseBehaviour extends OneShotBehaviour {

		MyCompositeBehaviour composite;

		public CountIncreaseBehaviour(MyCompositeBehaviour parent) {
			this.composite = parent;
		}

		@Override
		public void action() {
			if (composite.getCounter() == 5) {
				composite.block();
			} else {
				composite.increaseCounter();
			}
		}

	}


	/**
	 * Replies on the blocked or not status of the composite behaviour
	 * 
	 * @author bzafiris
	 *
	 */
	class CompositeBehaviourCheckResponder extends SimpleAchieveREResponder {

		MyCompositeBehaviour compositeBehaviour;

		public CompositeBehaviourCheckResponder(Agent a, MessageTemplate mt, MyCompositeBehaviour cmp) {
			super(a, mt);
			this.compositeBehaviour = cmp;
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {

			String cmd = request.getContent();
			ACLMessage response = request.createReply();

			if (cmd.equals("isBlocked")) {
				response.setPerformative(ACLMessage.INFORM);
				boolean isBlocked = !compositeBehaviour.isRunnable();
				response.setContent(Boolean.toString(isBlocked));
			} else if (cmd.equals("restart")) {
				response.setPerformative(ACLMessage.INFORM);
				response.setContent("ok");
				compositeBehaviour.increaseCounter();
				compositeBehaviour.restart();
			} else {
				response.setPerformative(ACLMessage.FAILURE);
				response.setContent("unknown command");
			}
			return response;
		}

	}

	class MyCompositeBehaviour extends CompositeBehaviour {

		int counter = 0;

		List<Behaviour> behaviours = new ArrayList<>();

		int currentBehaviour = 0;

		public MyCompositeBehaviour() {

		}

		public void addSubBehaviour(Behaviour b) {
			behaviours.add(b);
		}

		public void increaseCounter() {
			counter++;
			System.out.println(getLocalName() + " Counter: " + counter);
		}

		public int getCounter() {
			return counter;
		}

		@Override
		protected void scheduleFirst() {
			currentBehaviour = 0;
		}

		@Override
		protected void scheduleNext(boolean currentDone, int currentResult) {
			if (currentDone && isRunnable()) {
				currentBehaviour++;
			}
		}

		@Override
		protected boolean checkTermination(boolean currentDone, int currentResult) {

			if (currentBehaviour == behaviours.size()) {
				return true;
			}

			return false;
		}

		@Override
		protected Behaviour getCurrent() {
			
			if (currentBehaviour >= behaviours.size()){
				return null;
			}
			return behaviours.get(currentBehaviour);
		}

		@Override
		public Collection getChildren() {

			jade.util.leap.List children = new jade.util.leap.ArrayList();
			for (Behaviour b : behaviours) {
				children.add(b);
			}

			return children;
		}

	}

}
