package gr.aueb.jade.test.backendcontainer;

import java.util.HashSet;

import gr.aueb.jade.test.util.JadeUtility;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import test.common.Test;
import test.common.TestException;

public class BackendContainerTest extends Test implements BackendMethodCallback {

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";

	AID backendCallerAID = null;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		if (backendCallerAID == null) {
			backendCallerAID = JadeUtility.getAIDByLocalName(a, BACKEND_CALLER_LOCAL_NAME);
		}

		return new BackendMethodInvokerBehaviour(a, "agentNames", backendCallerAID, this);

	};

	public void onMethodResult(String methodName, String result) {

		if (methodName.equals("agentNames")) {
			String[] agentNames = result.split(";");

			if (agentNames.length != 4) {
				failed("Expected five agents in backend container");
				return;
			}

			HashSet<String> localNames = new HashSet<>();
			for (int i = 0; i < agentNames.length; i++) {
				localNames.add(agentNames[i].split("@")[0]);
			}

			if (localNames.contains("becaller") && localNames.contains("d1") && localNames.contains("d2")
					&& localNames.contains("pong")) {
				passed("Test passed");
			} else {
				failed("Agent names mismatch...");
			}

		}


	}

}
