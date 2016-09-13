package gr.aueb.jade.test.initiator;

import gr.aueb.jade.test.BaseTestGroup;
import gr.aueb.jade.test.BaseTesterAgent;
import gr.aueb.jade.test.agent.ServiceCaller;
import jade.core.AID;
import jade.core.Agent;
import jade.tools.rma.rma;
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

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "initiator//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {
				
				AID responderAgent = TestUtility.createAgent(a, "responderAgent",
						ResponderAgent.class.getName(), new Object[] {}, null, MAIN_CONTAINER_NAME);
				
				AID rma = TestUtility.createAgent(a, "rma",
						rma.class.getName(), new Object[] {}, null, MAIN_CONTAINER_NAME);
			}

			@Override
			protected void shutdown(Agent a) {

				killAgent("responderAgent");
				killAgent("rma");

			}
		};
		return bg;
	}

}
