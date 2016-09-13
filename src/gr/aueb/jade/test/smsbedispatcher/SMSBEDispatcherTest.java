package gr.aueb.jade.test.smsbedispatcher;

import java.io.IOException;
import java.net.Socket;

import javax.imageio.plugins.bmp.BMPImageWriteParam;

import org.junit.Assert;

import gr.aueb.jade.test.agent.DelayBehaviour;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import gr.aueb.jade.test.agent.ServiceInvocationCallback;
import gr.aueb.jade.test.agent.ServiceInvokerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.imtp.leap.JICP.JICPPacket;
import test.common.Test;
import test.common.TestException;

public class SMSBEDispatcherTest extends Test implements ServiceInvocationCallback {

	String methodName;

	public static String BACKEND_CALLER_LOCAL_NAME = "becaller";

	AID serviceCaller = null;

	static final String METHOD_SEND_SMS_MESSAGE = "sendSmsMessage";
	static final String METHOD_REQUEST_DROPDOWN = "requestDropDown";

	boolean rmaVisibleAfterMove = false;

	boolean correctMessageReceived = false;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID serviceCaller = new AID("serviceCaller", false);

		ServiceInvokerBehaviour serviceInvokerBehaviour = new ServiceInvokerBehaviour(a, METHOD_SEND_SMS_MESSAGE,
				serviceCaller, SMSBEDispatcherTest.this);

		DelayBehaviour delay2000 = new DelayBehaviour(2000);

		new Thread(new SMSReceiver()).start();

		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(delay2000);
		sb.addSubBehaviour(serviceInvokerBehaviour);
		sb.addSubBehaviour(delay2000);
		sb.addSubBehaviour(new AssertionBehaviour());

		return sb;
	}

	class SMSReceiver implements Runnable {

		@Override
		public void run() {

			// Client Phone: create a socket that represents a phone connected
			// to the sms manager
			Socket phoneSocket;
			try {
				phoneSocket = new Socket(Profile.getDefaultNetworkName(), 1100);

				while (!phoneSocket.isConnected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Helper phone waiting to connect...");
				}

				// Client phone: read the sms as a JICPPacket from the socket
				JICPPacket pkt = JICPPacket.readFrom(phoneSocket.getInputStream());
				String msg = new String(pkt.getData());
				if (msg.equals("hello")) {
					correctMessageReceived = true;
				}
				// assert that the read data are the same with the sms content
				phoneSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	class AssertionBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			if (correctMessageReceived) {
				passed("SMSBEDispatcher.init test passed!");
			} else {
				failed("SMSBEDispatcher.init test failed!");
			}
			System.out.println("Test assertions");
		}

	}

	@Override
	public void onMethodResult(String methodName, String result) {
		System.out.println("ServiceCallerFE result:" + result);
	}

}
