package gr.aueb.jade.test.backendcontainer;

import gr.aueb.jade.test.util.JadeUtility;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import test.common.Test;
import test.common.TestException;

public class BackendContainerTest2 extends Test implements BackendMethodCallback {

	private static final long serialVersionUID = -6524704323158216278L;

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";
	
	AID backendCallerAID = null;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		if (backendCallerAID == null) {
			backendCallerAID = JadeUtility.getAIDByLocalName(a, BACKEND_CALLER_LOCAL_NAME);
		}

		return new BackendMethodInvokerBehaviour(a, "postMessageToLocalAgent", backendCallerAID, this);

	};

	public void onMethodResult(String methodName, String result) {

		if (methodName.equals("postMessageToLocalAgent")) {
			System.out.println("Result=" + result);
			
			if (result.equals("true")){
				passed("postMessageToLocalAgent test passed");
			} else {
				failed("postMessageToLocalAgent test failed");
			}
		}

	}

}
