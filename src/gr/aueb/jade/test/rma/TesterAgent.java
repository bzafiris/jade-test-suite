package gr.aueb.jade.test.rma;

import gr.aueb.jade.test.BaseTestGroup;
import gr.aueb.jade.test.BaseTesterAgent;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import gr.aueb.jade.test.agent.ServiceCaller;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.mobility.MobilityOntology;
import jade.tools.rma.rma;
import test.common.JadeController;
import test.common.TestException;
import test.common.TestGroup;
import test.common.TestUtility;

public class TesterAgent extends BaseTesterAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {

		super.setup();

		getContentManager().registerOntology(MobilityOntology.getInstance());

	}

	@Override
	protected TestGroup getTestGroup() {

		BaseTestGroup bg = new BaseTestGroup(BASE_TEST_PACKAGE + "rma//testList.xml") {
			@Override
			protected void initialize(Agent a) throws TestException {

				boolean res1, res2;

				res1 = startContainer(a, BASE_RESOURCES_DIR + "rma//container0.txt");

				PlatformConfigurator pConfig = (PlatformConfigurator) getArgument("PlatformConfigurator[0]");

				AID serviceCallerAgent = TestUtility.createAgent(a, "serviceCaller", ServiceCaller.class.getName(),
						new Object[] {}, null, pConfig.getContainerName());

				AID rma = TestUtility.createAgent(a, "rma", rma.class.getName(), new Object[] {}, null,
						MAIN_CONTAINER_NAME);

			}

			@Override
			protected void shutdown(Agent a) {

				JadeController jc = (JadeController) getArgument("JadeController[0]");
				jc.kill();

			}
		};
		return bg;
	}

}
