package gr.aueb.jade.test.backendcontainer;

import gr.aueb.jade.test.BaseTestGroup;
import gr.aueb.jade.test.BaseTesterAgent;
import gr.aueb.jade.test.agent.DummyAgent;
import gr.aueb.jade.test.agent.PongAgent;
import jade.core.Agent;
import jade.core.MicroRuntime;
import test.common.TestException;
import test.common.TestGroup;

public class TesterAgent extends BaseTesterAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {

		super.setup();

	}


	@Override
	protected TestGroup getTestGroup() {

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "backendcontainer//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {

				boolean res1, res2;

				System.out.println("Starting micro container (" + getName() + ")");

				res1 = startMicroContainer("localhost", "1099", "gr.aueb.jade.test.backendcontainer.TestServiceFE",
						"gr.aueb.jade.test.backendcontainer.TestService",
						new String []{
						"becaller:" + BackendServiceCaller.class.getName(),
						"d1:" + DummyAgent.class.getName(),
						"d2:" + DummyAgent.class.getName(),
						"pong:" + PongAgent.class.getName()
						
						});

				if (!res1) {
					System.out.println("Error!Could not initialize container ...");
					return;
				}

			}

			@Override
			protected void shutdown(Agent a) {
				
				MicroRuntime.stopJADE();
				
			}
		};
		return bg;
	}

}
