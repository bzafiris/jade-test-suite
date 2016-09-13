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
import jade.imtp.leap.JICP.MaskableJICPPeer;
import jade.mtp.TransportAddress;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class MaskableJICPPeerTest {

	MaskableJICPPeer maskableJicpPeer;
	SSLContext contextWithAuth;

	@Before
	public void setup() {

	}

	public void assertCommand(String cmd) {
		System.out.println("MaskableJICPPeer Received command " + cmd);
		Assert.assertEquals(cmd, "hello");
	}

	

	@Test
	public void testActivate() {

		try {

			maskableJicpPeer = new MaskableJICPPeer();

			maskableJicpPeer.activate(new ICP.Listener() {

				@Override
				public byte[] handleCommand(byte[] cmdPayload) throws LEAPSerializationException {
					String cmd = new String(cmdPayload);
					assertCommand(cmd);
					try {
						maskableJicpPeer.deactivate();
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
			Connection conn = maskableJicpPeer.getConnectionFactory().createConnection(localTA);

			JICPPacket pkt = new JICPPacket(JICPProtocol.COMMAND_TYPE, JICPProtocol.DEFAULT_INFO, cmd.getBytes());

			conn.writePacket(pkt);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
