package gr.aueb.jade.test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.Specifier;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import test.common.Test;

public class TestMain {

	private static final String RMA_CLASS = "jade.tools.rma.rma";
	private static final String TEST_SUITE_AGENT_CLASS = "test.common.testSuite.TestSuiteAgent";
	public static final String TESTER_LIST_FILE = "gr//aueb//jade//test//testerList.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Get a hold on JADE runtime
			Runtime rt = Runtime.instance();

			// Exit the JVM when there are no more containers around
			rt.setCloseVM(true);

			String hostName = "localhost";

			Profile pMain = new ProfileImpl(hostName, Test.DEFAULT_PORT, null);
			
			AgentContainer mc = rt.createMainContainer(pMain);

			AgentController tester = mc.createNewAgent("testSuite", TEST_SUITE_AGENT_CLASS,
					new String[] { TESTER_LIST_FILE });
			tester.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
