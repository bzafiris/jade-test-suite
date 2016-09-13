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

public class TestMainNIO {

	private static final String RMA_CLASS = "jade.tools.rma.rma";
	private static final String TEST_SUITE_AGENT_CLASS = "test.common.testSuite.TestSuiteAgent";
	public static final String TESTER_LIST_FILE = "gr//aueb//jade//test//testerListNIO.xml";

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

			enableNIODispatcher(pMain);
			
			AgentContainer mc = rt.createMainContainer(pMain);

			AgentController tester = mc.createNewAgent("testSuite", TEST_SUITE_AGENT_CLASS,
					new String[] { TESTER_LIST_FILE });
			tester.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void enableNIODispatcher(Profile pMain) {
		ArrayList list = new ArrayList();
		Specifier spec = null;

		spec = new Specifier();
		spec.setClassName("jade.imtp.leap.nio.NIOJICPPeer");
		list.add(spec);

		pMain.setSpecifiers("icps", list);
		
		spec = new Specifier();
		spec.setClassName("jade.imtp.leap.nio.BEManagementService");

		ArrayList specifiersList = new ArrayList();
		specifiersList.add(spec);
		pMain.setSpecifiers(Profile.SERVICES, specifiersList);
		
//		pMain.setParameter("mediator-class", "jade.imtp.leap.sms.SMSBEDispatcher");
		
		pMain.setParameter("jade_imtp_leap_nio_BEManagementService_servers", "s1");
//		pMain.setParameter("msisdn", "mymsisdn");
	}

}
