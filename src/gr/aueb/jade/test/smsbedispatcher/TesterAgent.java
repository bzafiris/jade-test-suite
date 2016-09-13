package gr.aueb.jade.test.smsbedispatcher;

import gr.aueb.jade.test.BaseTestGroup;
import gr.aueb.jade.test.BaseTesterAgent;
import gr.aueb.jade.test.TestMain;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import jade.MicroBoot;
import jade.core.AID;
import jade.core.Agent;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import test.common.Test;
import test.common.TestException;
import test.common.TestGroup;
import test.common.TestUtility;

public class TesterAgent extends BaseTesterAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {

		super.setup();

	}

	@Override
	protected TestGroup getTestGroup() {

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "smsbedispatcher//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {

				boolean res1, res2;

				// start another main container with nio

				System.out.println("Starting micro container (" + getName() + ")");

				AID rma = TestUtility.createAgent(a, "rma", rma.class.getName(), new Object[] {}, null,
						MAIN_CONTAINER_NAME);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// initializes a ServiceCaller agent in the FrontEnd to dispatch TestService commands
				res1 = startMicroContainer(BASE_RESOURCES_DIR + "smsbedispatcher//split_container.txt");

				if (!res1) {
					System.out.println("Error!Could not initialize container ...");
					return;
				}

			}

			@Override
			protected void shutdown(Agent a) {

				killAgent("rma");
				
				MicroRuntime.stopJADE();

			}
		};
		return bg;
	}

}
