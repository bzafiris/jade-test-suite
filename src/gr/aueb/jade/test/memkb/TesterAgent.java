package gr.aueb.jade.test.memkb;

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

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "memkb//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {

			}

			@Override
			protected void shutdown(Agent a) {


			}
		};
		return bg;
	}

}
