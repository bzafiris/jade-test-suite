package gr.aueb.jade.test.service;

import jade.core.Agent;
import jade.core.ServiceHelper;

public class TestServiceFEHelper implements ServiceHelper {

	TestServiceFE service;
	
	public TestServiceFEHelper(TestServiceFE testServiceFE) {
		this.service = testServiceFE;
	}

	@Override
	public void init(Agent a) {

	}
	
	public String sendSmsMessage(String actor) {
		return service.sendSmsMessage(actor);
	}

	public String requestDropDown(String actor) {
		return service.requestDropDown(actor);
	}

}
