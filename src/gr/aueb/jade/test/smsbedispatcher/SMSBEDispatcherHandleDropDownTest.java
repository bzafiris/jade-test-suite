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
import jade.imtp.leap.JICP.JICPProtocol;
import test.common.Test;
import test.common.TestException;

public class SMSBEDispatcherHandleDropDownTest extends Test {

	boolean correctMessageReceived = false;

	@Override
	public Behaviour load(final Agent a) throws TestException {

		AID serviceCaller = new AID("serviceCaller", false);

		DelayBehaviour delay2000 = new DelayBehaviour(10000);

		new SMSSender().run();

		return new AssertionBehaviour();

	}

	class AssertionBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			if (correctMessageReceived) {
				passed("SMSBEDispatcher.handleDropDown test passed!");
			} else {
				failed("SMSBEDispatcher.handleDropDown test failed!");
			}
			System.out.println("Test assertions");
		}

	}

	class SMSSender implements Runnable {

		@Override
		public void run() {

			// Client Phone: create a socket that represents a phone connected
			// to the sms manager
			Socket clientSocket;
			try {
				clientSocket = new Socket(Profile.getDefaultNetworkName(), 2099);

				while (!clientSocket.isConnected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Helper phone waiting to connect...");
				}

				// Client : send drop down JICP packet
				JICPPacket pkt = new JICPPacket(JICPProtocol.DROP_DOWN_TYPE, JICPProtocol.TERMINATED_INFO,
						Integer.toString(1100).getBytes());
				pkt.setRecipientID("mymsisdn");
				int writtenBytes = pkt.writeTo(clientSocket.getOutputStream());

				// Client: receive drop down ACK message (handleDropDown has
				// been executed successfully)
				JICPPacket res = JICPPacket.readFrom(clientSocket.getInputStream());
				System.out.println("<---------------------" + res.getData());

				if (res.getType() == JICPProtocol.RESPONSE_TYPE && res.getInfo() == JICPProtocol.DEFAULT_INFO
						&& res.getData() == null) {
					correctMessageReceived = true;
				}

				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
