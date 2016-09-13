package jade.imtp.leap.sms;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import gr.aueb.jade.test.BaseTesterAgent;
import gr.aueb.jade.test.agent.PlatformConfigurator;
import jade.core.ProfileImpl;
import jade.imtp.leap.JICP.JICPPacket;
import org.junit.Assert;

public class PhoneBasedSMSManagerTest {

	@Test
	public void testInit() throws Exception {
		
		PlatformConfigurator pConfig = new PlatformConfigurator(true);
		pConfig.loadConfiguration(BaseTesterAgent.BASE_RESOURCES_DIR +
				 "smsmanager//container0.txt");
		
		ProfileImpl p = (ProfileImpl) pConfig.getProfile();
		
		// Server platform
		PhoneBasedSMSManager smsManager = (PhoneBasedSMSManager) PhoneBasedSMSManager.getInstance(p.getProperties());

		try {
			// Client Phone: create a socket that represents a phone connected to the sms manager
			Socket phoneSocket = new Socket(pConfig.getPlatformLocalHost(), 1100);
			
			while(!phoneSocket.isConnected()){
				Thread.sleep(1000);
				System.out.println("Phone waiting to connect...");
			}
			
			Thread.sleep(3000);
			
			// Server platform: send an sms to the connected phone 
			smsManager.sendTextMessage("msisdn", 1100, "hello");
			
			// Client phone: read the sms as a JICPPacket from the socket
			JICPPacket pkt = JICPPacket.readFrom(phoneSocket.getInputStream());
			
			// assert that the read data are the same with the sms content
			Assert.assertEquals("hello", new String(pkt.getData()));
			
			phoneSocket.close();
			smsManager.shutDown();
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	}
	
	
	
	
}
