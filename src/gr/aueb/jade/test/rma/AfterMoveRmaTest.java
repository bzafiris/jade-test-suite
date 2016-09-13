package gr.aueb.jade.test.rma;

import gr.aueb.jade.test.agent.DelayBehaviour;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import gr.aueb.jade.test.agent.ServiceInvocationCallback;
import gr.aueb.jade.test.agent.ServiceInvokerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import test.common.Test;
import test.common.TestException;

public class AfterMoveRmaTest extends Test implements RequestOutcomeCallback, ServiceInvocationCallback {

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";

	AID serviceCaller = null;

	static final String METHOD_IS_RMA_VISIBLE = "isRmaVisible";

	boolean rmaVisibleAfterMove = false;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		PlatformConfigurator pConfig = (PlatformConfigurator) getGroupArgument("PlatformConfigurator[0]");
		AID ams = (AID) getGroupArgument("AMS_0");
		AID serviceCaller = new AID("serviceCaller", false);

		RequestRmaMoveAction requestMoveAction = new RequestRmaMoveAction(a, null, ams, AfterMoveRmaTest.this,
				pConfig.getContainerName());

		ServiceInvokerBehaviour serviceInvokerBehaviour = new ServiceInvokerBehaviour(a, METHOD_IS_RMA_VISIBLE,
				serviceCaller, AfterMoveRmaTest.this);

		DelayBehaviour db = new DelayBehaviour(2000), db2 = new DelayBehaviour(2000), db3 = new DelayBehaviour(5000);


		AssertionBehaviour assertionBehaviour = new AssertionBehaviour();

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(requestMoveAction);
		sb.addSubBehaviour(db);
		sb.addSubBehaviour(serviceInvokerBehaviour);
		sb.addSubBehaviour(db2);
		sb.addSubBehaviour(assertionBehaviour);

		return sb;
	}

	class AssertionBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			if (rmaVisibleAfterMove) {
				passed("Rma clone test passed!");
			} else {
				failed("Rma clone test failed!");
			}
			System.out.println("Test assertions");
		}

	}

	/**
	 * Callbacks from RequestRmaAction
	 */
	public void onInform(String content) {

	}

	public void onFailure(String conent) {

	}

	/**
	 * Callbacks from ServiceInvokeBehaviour
	 */
	@Override
	public void onMethodResult(String methodName, String result) {

		if (methodName.equals(METHOD_IS_RMA_VISIBLE) && result.equals("true")) {
			rmaVisibleAfterMove = true;
		}

	};

}
