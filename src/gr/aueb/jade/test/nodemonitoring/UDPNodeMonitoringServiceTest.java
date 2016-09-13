package gr.aueb.jade.test.nodemonitoring;

import java.io.IOException;

import org.junit.Assert;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.behaviours.Behaviour;
import jade.core.nodeMonitoring.MyUDPMonitorClient;
import jade.core.nodeMonitoring.PingFailedError;
import jade.domain.DFMemKB;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.util.leap.List;
import test.common.Test;
import test.common.TestException;

public class UDPNodeMonitoringServiceTest extends Test {

	boolean testPassed = false;
	
	@Override
	public Behaviour load(final Agent a) throws TestException {

		MyUDPMonitorClient client = new MyUDPMonitorClient(Profile.getDefaultNetworkName(), 28000, 1000, 1000);
		
		try {
			client.start();
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			client.stop(false);
			testPassed = true;
		} catch (PingFailedError | IOException e) {
			e.printStackTrace();
		}
		
		return new Behaviour(a) {
			
			
			
			@Override
			public boolean done() {
				return true;
			}

			@Override
			public void action() {
			
				if (testPassed){
					passed("UDP test passed!");
				} else {
					failed("Ping failed");
				}
				
			}

		};

	}
}
