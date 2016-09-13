package gr.aueb.jade.test.backendcontainer;

import jade.core.Agent;
import jade.core.ServiceHelper;

public abstract class TestServiceFEHelper implements ServiceHelper {

	@Override
	public void init(Agent a) {

	}
	
	/**
	 * Find the names of agents available from the backend container
	 * 
	 * @param actor The agent invoking the method
	 * @return An array of agent names
	 */
	abstract public String[] getAgentNames(String actor);
	
	abstract boolean postMessageToLocalAgent(String actor);

}
