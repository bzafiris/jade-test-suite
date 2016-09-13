package gr.aueb.jade.test.service;

import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.BackEndContainer;
import jade.core.BaseService;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

public class TestService extends BaseService {

	AgentContainer agentContainer;

	@Override
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		super.init(ac, p);
		agentContainer = ac;

	}

	public ServiceHelper getHelper(Agent a) throws ServiceException {
		// The agent is passed to the helper in the init() method
		System.out.println("Creating instance of TestService helper");
		return new TestServiceHelper(agentContainer, a);
	}
	

	@Override
	public String getName() {
		return TestService.class.getName();
	}

}
