package gr.aueb.jade.test.composite;

import gr.aueb.jade.test.BaseTestGroup;
import gr.aueb.jade.test.BaseTesterAgent;
import jade.core.AID;
import jade.core.Agent;
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

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "composite//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {

				boolean res1, res2;

				AID compositeBehaviourAgent = TestUtility.createAgent(a, "compositeAgent",
						CompositeBehaviourAgent.class.getName(), new Object[] {});
				
				AID compositeBehaviourAgent2 = TestUtility.createAgent(a, "compositeAgent2",
						CompositeBehaviourAgent.class.getName(), new Object[] {});

			}

			@Override
			protected void shutdown(Agent a) {

				killAgent("compositeAgent");
				killAgent("compositeAgent2");
				
			}
		};
		return bg;
	}

}
