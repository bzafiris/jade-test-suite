package gr.aueb.jade.test;

import java.rmi.Naming;

import gr.aueb.jade.test.agent.PlatformConfigurator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.MicroRuntime;
import jade.core.ProfileImpl;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.util.leap.Properties;
import test.common.JadeController;
import test.common.TestException;
import test.common.TestGroup;
import test.common.TestUtility;
import test.common.remote.RemoteManager;

public class BaseTestGroup extends TestGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4600360235536870651L;

	public static int PLATFORM_ID = 0;

	protected String m_sConfFile = "./tester/conf.txt";

	public BaseTestGroup(String filename) {
		super(filename);
	}

	@Override
	protected void initialize(Agent a) throws TestException {

	}

	protected boolean startMicroContainer(String configurationFile) {

		PlatformConfigurator configurator = new PlatformConfigurator(false);
		configurator.loadConfiguration(configurationFile);

		String maxDiscTime = "5000";

		ProfileImpl profile = (ProfileImpl) configurator.getProfile();

		Properties pp = profile.getProperties();

		pp.setProperty(JICPProtocol.KEEP_ALIVE_TIME_KEY, "-1");

		MicroRuntime.startJADE(pp, new Runnable() {
			@Override
			public void run() {

				System.out.println("Shutting down platform");

			}
		});

		return true;
	}

	protected boolean startMicroContainer(String mediatorHost, String mediatorPort, String frontEndServices,
			String backendRequiredServices, String[] agents) {

		String maxDiscTime = "5000";

		Properties pp = new Properties();

		if (mediatorHost != null) {
			pp.setProperty(MicroRuntime.HOST_KEY, mediatorHost);
		}

		if (mediatorPort != null) {
			pp.setProperty(MicroRuntime.PORT_KEY, mediatorPort);
		}

		if (maxDiscTime != null) {
			pp.setProperty(JICPProtocol.MAX_DISCONNECTION_TIME_KEY, maxDiscTime);
		}

		if (frontEndServices != null)
			pp.setProperty("services", frontEndServices);

		if (backendRequiredServices != null)
			pp.setProperty("be-required-services", backendRequiredServices);

		if (agents != null && agents.length > 0) {

			StringBuffer agentNames = new StringBuffer();
			for (String s : agents) {
				agentNames.append(s + ';');
			}
			agentNames.deleteCharAt(agentNames.length() - 1);
			pp.setProperty(MicroRuntime.AGENTS_KEY, agentNames.toString());
		}

		pp.setProperty(JICPProtocol.KEEP_ALIVE_TIME_KEY, "-1");

		MicroRuntime.startJADE(pp, new Runnable() {
			@Override
			public void run() {

				System.out.println("Shutting down platform");

			}
		});

		return true;
	}

	/**
	 * Start a container attached to an existing main container
	 * 
	 * @param a
	 * @param sPlatformConfig
	 * @param sAgentConfig
	 * @return
	 */
	protected boolean startContainer(Agent a, String sPlatformConfig) {

		PlatformConfigurator m_pConfig = new PlatformConfigurator(true);

		m_pConfig.loadConfiguration(sPlatformConfig);

		// Host name and port of the main container
		m_pConfig.setPlatformId(m_pConfig.getPlatformMainHost(), m_pConfig.getPlatformMainPort());

		setArgument("PlatformConfigurator[" + PLATFORM_ID + "]", m_pConfig);

		JadeController jc = null;

		String localContainerHost = m_pConfig.getPlatformLocalHost();
		String mainContainerHost = m_pConfig.getPlatformMainHost();

		if (!localContainerHost.equals(mainContainerHost)) {

			m_pConfig.setPlatformLocalHost(mainContainerHost);
			try {
				RemoteManager manager = (RemoteManager) Naming.lookup("rmi://" + mainContainerHost + ":7777//TSDaemon");

				jc = initializeEmptyAgentPlatform(manager, a, m_pConfig);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			jc = initializeEmptyAgentPlatform(null, a, m_pConfig);
		}

		setArgument("JadeController[" + PLATFORM_ID + "]", jc);

		PLATFORM_ID++;

		return true;
	}

	@Override
	protected void shutdown(Agent a) {
		JadeController jc = (JadeController) getArgument("JadeController[0]");
		jc.kill();

	}

	// TODO Move the following methods to the BaseTestGroup class
	public static AID getLocalAMS(JadeController jc, PlatformConfigurator pConfig) {
		AID ams = new AID();

		String sPlatformId = pConfig.getPlatformId();

		ams.setName("ams@" + sPlatformId);
		List addresses = jc.getAddresses();
		String sAddress = null;
		Iterator it = addresses.iterator();
		while (it.hasNext()) {
			sAddress = (String) it.next();
			System.out.println("Address:" + sAddress);
			ams.addAddresses(sAddress);
		}
		return ams;
	}


	protected JadeController initializeEmptyAgentPlatform(RemoteManager rm, Agent a, PlatformConfigurator pConfig) {

		JadeController jc = null;
		try {
			if (rm != null) {
				jc = TestUtility.launchJadeInstance(rm, pConfig
						.getPlatformId(), /* PlatformConfigurator.CLASSPATH */
						null, pConfig.toCmdLineArgsString(), new String[] { "http" });
			} else {
				// Launch local platform
				jc = TestUtility.launchJadeInstance(pConfig.getPlatformId(), null, pConfig.toCmdLineArgsString(),
						new String[] { "http" });
			}
			AID ams = getLocalAMS(jc, pConfig);
			setArgument("AMS_" + PLATFORM_ID, ams);

		} catch (TestException e) {
			e.printStackTrace();
			return null;
		}
		return jc;
	}

}
