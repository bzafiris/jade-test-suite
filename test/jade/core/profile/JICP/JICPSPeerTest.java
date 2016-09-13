package jade.core.profile.JICP;

import javax.net.ssl.SSLContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jade.core.ProfileImpl;
import jade.imtp.leap.ICP;
import jade.imtp.leap.ICPException;
import jade.imtp.leap.LEAPSerializationException;
import jade.imtp.leap.JICP.Connection;
import jade.imtp.leap.JICP.JICPPacket;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.imtp.leap.JICP.JICPSPeer;
import jade.mtp.TransportAddress;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class JICPSPeerTest {

	JICPSPeer jicpsPeer;
	SSLContext contextWithAuth;

	@Before
	public void setup() {

	}

	public void assertCommand(String cmd) {
		System.out.println("JICPSPeer Received command " + cmd);
		Assert.assertEquals(cmd, "hello");
	}

	

	@Test
	public void testActivate() {

		System.setProperty("javax.net.ssl.keyStore", "resources//server.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "jade123");
		System.setProperty("com.sun.net.ssl.checkRevocation", "false");

		try {

			jicpsPeer = new JICPSPeer();

			jicpsPeer.activate(new ICP.Listener() {

				@Override
				public byte[] handleCommand(byte[] cmdPayload) throws LEAPSerializationException {
					String cmd = new String(cmdPayload);
					assertCommand(cmd);
					try {
						jicpsPeer.deactivate();
					} catch (ICPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO Auto-generated method stub
					return cmdPayload;
				}
			}, "myPeerId", new ProfileImpl());
		} catch (ICPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String cmd = "hello";

		try {

			TransportAddress localTA = JICPProtocol.getInstance().buildAddress("localhost", "1099", null, null);
			Connection conn = jicpsPeer.getConnectionFactory().createConnection(localTA);

			JICPPacket pkt = new JICPPacket(JICPProtocol.COMMAND_TYPE, JICPProtocol.DEFAULT_INFO, cmd.getBytes());

			conn.writePacket(pkt);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
