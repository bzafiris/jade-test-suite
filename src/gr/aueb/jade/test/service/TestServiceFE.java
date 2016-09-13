package gr.aueb.jade.test.service;

import jade.core.Agent;
import jade.core.FEService;
import jade.core.IMTPException;
import jade.core.NotFoundException;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

/**
 * Helper methods that are invoked in order to test the BackEnd container in case of a split container configuration
 * @author bzafiris
 *
 */
public class TestServiceFE extends FEService {

	@Override
	public String getName() {
		return TestService.class.getName();
	}

	public String sendSmsMessage(String actor) {
		try {
			return (String) invoke(actor, "sendSmsMessage", new Object[] {});
		} catch (ServiceException | IMTPException | NotFoundException e) {
			e.printStackTrace();
			return Boolean.toString(false);
		}
	}
	
	public String requestDropDown(String actor) {
		try {
			return (String) invoke(actor, "requestDropDown", new Object[] {});
		} catch (ServiceException | IMTPException | NotFoundException e) {
			e.printStackTrace();
			return Boolean.toString(false);
		}
	}
	
	@Override
	public ServiceHelper getHelper(Agent a) {
		return new TestServiceFEHelper(this);
	}

}
