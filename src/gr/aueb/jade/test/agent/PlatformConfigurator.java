package gr.aueb.jade.test.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import gr.aueb.jade.test.util.MyFile;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Specifier;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class PlatformConfigurator {

	ProfileImpl profileImpl = null;

	// Not used yet...
	public static String CLASSPATH = "./bin;./lib/Base64.jar;./lib/jade.jar;./lib/jadeTools.jar;./lib/BEFipaMessage.jar;./lib/commons-codec-1.3.jar;./lib/crimson.jar;./lib/http.jar;./lib/iiop.jar;";

	public static String[] PROTONAMES = { "jade.mtp.http.MessageTransportProtocol" };
	public static String[] SERVICES = { "jade.core.event.NotificationService",
			"jade.core.mobility.AgentMobilityService"/*, "jade.core.migration.InterPlatformMobilityService"*/ };

	private static int LOCAL_PORT = 1123;

	private static String PLATFORM_PREFIX = "JADE_";

	private static int HTTP_MTP_PORT = 7778;

	public boolean bShowGUI = true;

	private String m_sHostName;

	private String mainContainerHost = null;

	private int m_nPort;

	public Profile getProfile() {
		return profileImpl;
	}

	private boolean bContainer;

	public PlatformConfigurator(boolean bLoadDefaults) {
		profileImpl = new ProfileImpl();
		if (bLoadDefaults) {
			addServices(SERVICES);
			addMTProtocols(PROTONAMES);
			setLocalPort(LOCAL_PORT);
			m_nPort = LOCAL_PORT;
			setPlatformLocalHost(Profile.getDefaultNetworkName());
			// setPlatformLocalHost("127.0.0.1");
			setPlatformDefaultId();
//			profileImpl.setParameter(InterPlatformMobilityService.MIGRATION_TIMEOUT, "100000");
			profileImpl.setParameter("jade_core_management_AgentManagementService_agentspath", "jars");
		}
	}

	public String getMainContainerHost() {
		return mainContainerHost;
	}

	public void loadConfiguration(String sFileName) {
		MyFile f = new MyFile(sFileName);
		f.openFile(false);
		Properties p = f.readProperties();

		mainContainerHost = p.getProperty("host", "localhost");
		setPlatformMainHost(mainContainerHost);

		String mainPort = p.getProperty("port", "1099");
		setPlatformMainPort(mainPort);

		String sLocalHost = p.getProperty("local-host");

		if (sLocalHost != null)
			setPlatformLocalHost(sLocalHost);
		if (p.containsKey("local-port"))
			setLocalPort(Integer.parseInt(p.getProperty("local-port")));
		if (p.containsKey("AgentsPath"))
			profileImpl.setParameter("jade_core_management_AgentManagementService_agentspath",
					p.getProperty("AgentsPath"));
		if (p.containsKey("HTTPPort"))
			setHttpMtpPort(Integer.parseInt(p.getProperty("HTTPPort")));
		if (p.containsKey("ShowGUI"))
			bShowGUI = Boolean.parseBoolean(p.getProperty("ShowGUI"));

		if (p.containsKey("container")) {
			bContainer = Boolean.parseBoolean(p.getProperty("container", "false"));
		}

		if (p.containsKey(Profile.CONTAINER_NAME)) {
			profileImpl.setParameter(Profile.CONTAINER_NAME, p.getProperty("container-name"));
		}

		if (bContainer) {
			profileImpl.setParameter(Profile.MAIN, Boolean.toString(bContainer));

		}

		if (p.containsKey("service")) {
			addService(p.getProperty("service"));
		}

		if (p.containsKey(Profile.SERVICES)) {
			String services = p.getProperty(Profile.SERVICES);
			addServices(services.split(";"));
		}

		if (p.containsKey("fe-services")) {
			String services = p.getProperty("fe-services");
			addFeServices(services);
		}

		if (p.containsKey(MicroRuntime.BE_REQUIRED_SERVICES_KEY)) {
			String services = p.getProperty(MicroRuntime.BE_REQUIRED_SERVICES_KEY);
			addBeServices(services);
		}

		if (p.containsKey(MicroRuntime.AGENTS_KEY)) {
			String agents = p.getProperty(MicroRuntime.AGENTS_KEY);
			addAgents(agents);
		}

		if (p.containsKey(JICPProtocol.MEDIATOR_CLASS_KEY)){
			profileImpl.setParameter(JICPProtocol.MEDIATOR_CLASS_KEY, p.getProperty(JICPProtocol.MEDIATOR_CLASS_KEY));
		}
		
		if (p.containsKey("msisdn")){
			profileImpl.setParameter("msisdn", p.getProperty("msisdn"));
		}
		
		if (p.containsKey("icps")){
			addICPS(p.getProperty("icps").split(";"));
		}
		
		if (p.containsKey(MicroRuntime.PROTO_KEY)){
			profileImpl.setParameter(MicroRuntime.PROTO_KEY, p.getProperty(MicroRuntime.PROTO_KEY));
		}
		
		if (p.containsKey("jade_imtp_leap_nio_BEManagementService_protocol")){
			profileImpl.setParameter("jade_imtp_leap_nio_BEManagementService_protocol", p.getProperty("jade_imtp_leap_nio_BEManagementService_protocol"));
		}
		
		System.out.println("Read configuration: " + sLocalHost + "," + p.getProperty("AgentsPath") + ","
				+ p.getProperty("HTTPPort") + "," + p.getProperty("ShowGUI"));
	}

	public String getContainerName() {
		return profileImpl.getParameter(Profile.CONTAINER_NAME, "Container");
	}

	public void addServices(String[] arServices) {
		ArrayList list = new ArrayList();
		Specifier spec = null;

		for (int i = 0; i < arServices.length; i++) {
			spec = new Specifier();
			spec.setClassName(arServices[i]);
			list.add(spec);
		}
		profileImpl.setSpecifiers(Profile.SERVICES, list);
	}
	
	public void addICPS(String[] arICPS) {
		ArrayList list = new ArrayList();
		Specifier spec = null;

		for (int i = 0; i < arICPS.length; i++) {
			spec = new Specifier();
			spec.setClassName(arICPS[i]);
			list.add(spec);
		}
		profileImpl.setSpecifiers("icps", list);
	}

	public void addBeServices(String arServices) {
		profileImpl.setParameter(MicroRuntime.BE_REQUIRED_SERVICES_KEY, arServices);
	}

	public void addFeServices(String arServices) {
		profileImpl.setParameter(MicroRuntime.SERVICES_KEY, arServices);
	}

	public void addAgents(String arAgents) {
		profileImpl.setParameter(MicroRuntime.AGENTS_KEY, arAgents);
	}

	public void addService(String service) {
		try {

			Specifier spec = new Specifier();
			spec.setClassName(service);

			List specifiersList = profileImpl.getSpecifiers(Profile.SERVICES);
			specifiersList.add(spec);
			profileImpl.setSpecifiers(Profile.SERVICES, specifiersList);

		} catch (ProfileException e) {
			e.printStackTrace();
		}
	}

	public void addMTProtocols(String[] arMTPs) {
		ArrayList list = new ArrayList();
		Specifier spec = null;

		for (int i = 0; i < arMTPs.length; i++) {
			spec = new Specifier();
			spec.setClassName(arMTPs[i]);
			list.add(spec);
		}
		profileImpl.setSpecifiers(Profile.MTPS, list);
	}

	public void enableBitEfficientEncoding() {
		profileImpl.setParameter(Profile.ACLCODECS, "sonera.fipa.acl.BitEffACLCodec");
	}

	public void setAgentsPath(String path) {
		profileImpl.setParameter("jade_core_management_AgentManagementService_agentspath", path);
	}

	public void setLocalPort(int nPort) {
		m_nPort = nPort;
		profileImpl.setParameter(Profile.LOCAL_PORT, Integer.toString(nPort));
		setPlatformDefaultId();
	}

	public String toCmdLineArgsString() {
		StringBuffer sCmdLine = new StringBuffer();

		if (bShowGUI) {
			sCmdLine.append(" -gui");
		}

		if (bContainer) {
			sCmdLine.append(" -container");
			sCmdLine.append(" ");

			sCmdLine.append(" -");
			sCmdLine.append(Profile.CONTAINER_NAME);
			sCmdLine.append(" ");
			sCmdLine.append(profileImpl.getParameter(Profile.CONTAINER_NAME, "CONTAINER"));
		}

		sCmdLine.append(" -");
		sCmdLine.append(Profile.SERVICES);
		sCmdLine.append(" ");
		try {
			sCmdLine.append(specsToString(profileImpl.getSpecifiers(Profile.SERVICES)));
		} catch (ProfileException e) {
			e.printStackTrace();
		}

		sCmdLine.append(" -");
		sCmdLine.append(Profile.MAIN_HOST);
		sCmdLine.append(" ");
		sCmdLine.append(profileImpl.getParameter(Profile.MAIN_HOST, "localhost"));

		sCmdLine.append(" -");
		sCmdLine.append(Profile.MAIN_PORT);
		sCmdLine.append(" ");
		sCmdLine.append(profileImpl.getParameter(Profile.MAIN_PORT, "1099"));

		sCmdLine.append(" -");
		sCmdLine.append(Profile.LOCAL_PORT);
		sCmdLine.append(" ");
		sCmdLine.append(profileImpl.getParameter(Profile.LOCAL_PORT, Integer.toString(LOCAL_PORT)));

		sCmdLine.append(" -");
		sCmdLine.append(Profile.LOCAL_HOST);
		sCmdLine.append(" ");
		sCmdLine.append(profileImpl.getParameter(Profile.LOCAL_HOST, "localhost"));

//		sCmdLine.append(" -");
//		sCmdLine.append(InterPlatformMobilityService.MIGRATION_TIMEOUT);
//		sCmdLine.append(" ");
//		sCmdLine.append(profileImpl.getParameter(InterPlatformMobilityService.MIGRATION_TIMEOUT, "5000"));

		sCmdLine.append(" -");
		sCmdLine.append("jade_core_management_AgentManagementService_agentspath");
		sCmdLine.append(" ");
		sCmdLine.append(profileImpl.getParameter("jade_core_management_AgentManagementService_agentspath", "jars"));

		String sPlatformId = getPlatformId();
		if (sPlatformId != null) {
			sCmdLine.append(" -");
			sCmdLine.append(Profile.PLATFORM_ID);
			sCmdLine.append(" ");
			sCmdLine.append(sPlatformId);
		}
		return sCmdLine.toString();

	}

	private String specsToString(List specifiers) {
		StringBuffer sSpecifiers = new StringBuffer();
		Iterator it = specifiers.iterator();
		Specifier spec = null;

		while (it.hasNext()) {
			spec = (Specifier) it.next();
			sSpecifiers.append(spec.getClassName());
			if (it.hasNext())
				sSpecifiers.append(";");
		}
		return sSpecifiers.toString();

	}

	public void setHttpMtpPort(int nPort) {
		profileImpl.setParameter("jade_mtp_http_port", Integer.toString(nPort));
	}

	public void setPlatformLocalHost(String sLocalHost) {

		if (sLocalHost.equals("localhost")) {
			sLocalHost = ProfileImpl.getDefaultNetworkName();
		}

		profileImpl.setParameter(Profile.LOCAL_HOST, sLocalHost);
	}

	public void setPlatformMainHost(String sMainHost) {

		if (sMainHost.equals("localhost")) {
			sMainHost = ProfileImpl.getDefaultNetworkName();
		}

		profileImpl.setParameter(Profile.MAIN_HOST, sMainHost);
	}

	public String getPlatformMainHost() {
		return profileImpl.getParameter(Profile.MAIN_HOST, "localhost");
	}

	public void setPlatformMainPort(String sMainPort) {
		profileImpl.setParameter(Profile.MAIN_PORT, sMainPort);
	}

	public int getPlatformMainPort() {
		return Integer.parseInt(profileImpl.getParameter(Profile.MAIN_PORT, "1099"));
	}

	public String getPlatformLocalHost() {
		return profileImpl.getParameter(Profile.LOCAL_HOST, "localhost");
	}

	public int getLocalPort() {
		return m_nPort;
	}

	public void setPlatformId(String hostName, int nPort) {
		String sPlatformId;
		m_sHostName = hostName;
		sPlatformId = hostName + ":" + Integer.toString(nPort) + "/JADE";
		profileImpl.setParameter(Profile.PLATFORM_ID, sPlatformId);
	}

	public String getPlatformId() {
		return profileImpl.getParameter(Profile.PLATFORM_ID, null);
	}

	public void setPlatformDefaultId() {
		try {
			m_sHostName = InetAddress.getLocalHost().getHostName();
			profileImpl.setParameter(Profile.PLATFORM_ID, m_sHostName + ":" + Integer.toString(m_nPort) + "/JADE");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
