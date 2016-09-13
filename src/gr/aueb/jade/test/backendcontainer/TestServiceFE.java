package gr.aueb.jade.test.backendcontainer;

import jade.core.Agent;
import jade.core.FEService;
import jade.core.IMTPException;
import jade.core.NotFoundException;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

public class TestServiceFE extends FEService {

	@Override
	public String getName() {
		return TestService.class.getName();
	}

	@Override
	public ServiceHelper getHelper(Agent a) {
		return new TestServiceFEHelper() {

			@Override
			public String[] getAgentNames(String actor) {
				try {
					return (String[]) invoke(actor, "agentNames", new Object[] {});
				} catch (ServiceException | IMTPException | NotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			boolean postMessageToLocalAgent(String actor) {
				try {
					return (Boolean) invoke(actor, "postMessageToLocalAgent", new Object[] {});
				} catch (ServiceException | IMTPException | NotFoundException e) {
					e.printStackTrace();
					return false;
				}
			}

		};
	}

}
