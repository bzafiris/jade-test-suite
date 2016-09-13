package gr.aueb.jade.test.rma;

import gr.aueb.jade.test.agent.DelayBehaviour;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import gr.aueb.jade.test.agent.ServiceInvocationCallback;
import gr.aueb.jade.test.agent.ServiceInvokerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import test.common.Test;
import test.common.TestException;

public class AfterCloneRmaTest extends Test implements RequestOutcomeCallback,
		ServiceInvocationCallback {

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";

	AID serviceCaller = null;

	static final String METHOD_IS_RMACLONE_VISIBLE = "isRmaCloneVisible";

	@Override
	public Behaviour load(final Agent a) throws TestException {

		PlatformConfigurator pConfig = (PlatformConfigurator) getGroupArgument("PlatformConfigurator[0]");
		AID ams = (AID) getGroupArgument("AMS_0");
		AID serviceCaller = new AID("serviceCaller", false);

		RequestRmaCloneAction requestCloneAction = new RequestRmaCloneAction(a,
				null, ams, AfterCloneRmaTest.this, pConfig.getContainerName());

		ServiceInvokerBehaviour serviceInvokerBehaviour = new ServiceInvokerBehaviour(
				a, METHOD_IS_RMACLONE_VISIBLE, serviceCaller,
				AfterCloneRmaTest.this);

		DelayBehaviour db = new DelayBehaviour(2000);

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(requestCloneAction);
		sb.addSubBehaviour(db);
		sb.addSubBehaviour(serviceInvokerBehaviour);

		return sb;

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

		if (methodName.equals(METHOD_IS_RMACLONE_VISIBLE)
				&& result.equals("true")) {
			passed("Rma clone test passed!");
		} else {
			failed("Rma clone test failed!");
		}

	};

}
