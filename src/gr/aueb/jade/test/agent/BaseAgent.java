package gr.aueb.jade.test.agent;

/*
 * Network.java
 *
 * Created on November 17, 2002, 8:53 PM
 */
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

/**
 * 
 * @author zafiris
 * @version
 * @modelguid {620E3B40-75D4-4F49-8F66-AF4EE7AAF68A}
 */
public class BaseAgent extends Agent {

//	public static boolean LOG_ENABLED = false;
	
	/** @modelguid {5C4D9EF2-75BD-4A03-8084-9439764FC0CA} */
	protected transient ContentManager manager = null;

	// This agent "speaks" the SL language
	/** @modelguid {B21C65D1-F74A-45FF-A3B9-82F7CA2E3EDA} */
	protected transient Codec codec = null;

	// This agent "knows" the Music-Shop ontology
	/** @modelguid {4D167216-4566-4C56-A20E-C66791B28B1F} */
	protected transient Ontology ontology = null;

	// Vector with the current connections to the network
	/** @modelguid {53484386-FCC4-405E-A746-4131E15F55AF} */
	protected transient AID m_aidMyself = null;


	// public static String AVAIL_NETS_CONV_ID = "AVAIL_NET_INFO";
	// public static String HOME_NET_CONV_ID = "HOME_NET_INFO";

	/** @modelguid {19294BE3-F3C8-4688-85CD-4680A265358A} */
	protected void setup() {
		manager = (ContentManager) getContentManager();
		codec = new SLCodec();
		ontology = BasicOntology.getInstance();
//		((BasicOntology)ontology).initialize();
		manager.registerLanguage(codec);
		manager.registerOntology(ontology);
		// manager.registerOntology(FIPAManagementOntology.getInstance());
		m_aidMyself = getAID();
		
	}

	/**
	 * Creates a message adopting the SL Language and the auth-ontology
	 * ontology.
	 * 
	 * @param msgPerformative
	 * @return
	 */
	public ACLMessage createMessage(int msgPerformative) {
		ACLMessage msg = new ACLMessage(msgPerformative);
		msg.setOntology(ontology.getName());
		msg.setLanguage(codec.getName());
		return msg;
	}


	/** @modelguid {0D6531BB-AFDB-4659-BE9D-6BD7899D8B3D} */
	protected void printDebug(String sMsg, boolean bDebug) {
		if (bDebug)
			System.out.println("<" + m_aidMyself.getLocalName() + ", "
					+ System.currentTimeMillis() + "> : " + sMsg);
	}

	/** @modelguid {9C5069B9-3313-445B-AF6B-DDA5D3F4D93F} */
	protected AID getAIDByLocalName(String sLocalName) {
		try {
			AMSAgentDescription amsDescr = new AMSAgentDescription();
			AMSAgentDescription[] res = null;
			amsDescr.setName(new AID(sLocalName, false));
			res = AMSService.search(this, amsDescr);
			if (res.length == 0)
				return null;
			return res[0].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
	}

	protected AID[] getAIDByServiceName(String sServiceName, long nHopCount) {
		try {
			AID[] aids = null;
			DFAgentDescription dfa = new DFAgentDescription();
			DFAgentDescription[] res = null;
			SearchConstraints constraints = new SearchConstraints();

			constraints.setMaxDepth(nHopCount);
			constraints.setMaxResults(10l);
			ServiceDescription sd = new ServiceDescription();
			sd.setName(sServiceName);

			dfa.addServices(sd);
			res = DFService.search(this, dfa, constraints);

			if (res.length == 0)
				return null;
			aids = new AID[res.length];
			for (int i = 0; i < res.length; i++)
				aids[i] = res[i].getName();
			return aids;
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
	}

	public static AID[] getAIDByServiceName(Agent requester, AID aidDF,
			String sServiceName, long nHopCount) {
		try {
			AID[] aids = null;
			DFAgentDescription dfa = new DFAgentDescription();
			DFAgentDescription[] res = null;
			SearchConstraints constraints = new SearchConstraints();

			constraints.setMaxDepth(nHopCount);
			constraints.setMaxResults(10l);
			
			ServiceDescription sd = new ServiceDescription();
			sd.setName(sServiceName);

			dfa.addServices(sd);
			res = DFService.search(requester, aidDF, dfa, constraints);

			if (res.length == 0)
				return null;
			aids = new AID[res.length];
			for (int i = 0; i < res.length; i++)
				aids[i] = res[i].getName();
			return aids;
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
	}

	/** @modelguid {AA4D82B0-D7D0-46DE-B13C-5BA1D554089D} */
	protected boolean offersService(String sLocalName, String sServiceName) {
		AID[] res = getAIDByServiceName(sServiceName, 1);
		String sCurrName = null;
		if (res.length == 0)
			return false;
		for (int i = 0; i < res.length; i++) {
			sCurrName = res[i].getLocalName();
			if (sCurrName.equals(sLocalName))
				return true;
		}
		return false;
	}

	/** @modelguid {C4D2A657-BF08-4A77-9BDD-E33498A2FFFF} */
	protected boolean registerDFService(String sLang, String sProtocol,
			String sName) {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(m_aidMyself);
			ServiceDescription sd = new ServiceDescription();
			sd.addLanguages(sLang);
			// sd.addLanguage(sLang);
			sd.addProtocols(sProtocol);
			sd.addOntologies(ontology.getName());
			// sd.addProtocol(sProtocol);
			sd.setName(sName);

			sd.setType(sName);
			dfd.addServices(sd);
			DFService.register(this, dfd);
			return true;
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return false;
		}
	}

	// public String getSupportedLanguage(){
	// return codec.getName();
	// }

	/** @modelguid {EC9A9E9D-2DB3-4027-B4EA-2124DBC8C0E6} */
	protected boolean killAgent(String sLocalName) {
		try {
			jade.wrapper.AgentContainer ac = getContainerController();
			AgentController agent = ac.getAgent(sLocalName);
			if (agent != null)
				agent.kill();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** @modelguid {CB53808E-1E1C-4CB7-B619-2B1FB4F1962C} */
	protected void onGuiEvent(jade.gui.GuiEvent guiEvent) {
	}

	/** @modelguid {DFA16BAA-F078-4862-8A61-0E5E7A5EDAE6} */
	protected class CheckMsgBeh extends CyclicBehaviour {

		/** @modelguid {ED044A46-35F5-4C00-AA1C-B3A6F8788978} */
		public CheckMsgBeh(Agent a) {
			super(a);
		}

		/** @modelguid {2C7872CB-F78C-459C-A9EE-FD75DBC2ABC8} */
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				System.out.println("Received msg from "
						+ msg.getSender().getLocalName() + " of type "
						+ msg.getPerformative(msg.getPerformative())
						+ " content: " + msg.getContent());
				myAgent.putBack(msg);
			}
		}
	}

	public Codec getCodec() {
		return codec;
	}

	public Ontology getOntology() {
		return ontology;
	}

	
	protected DFAgentDescription getDefaultDFAgentDescription() {
		AID df = getDefaultDF();
		return getDFAgentDescription(df);
	}

	protected static DFAgentDescription getDFAgentDescription(AID aidDF) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(aidDF);
		dfd.setLeaseTime(null);
		dfd.addOntologies(FIPAManagementOntology.NAME);
		dfd.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
		dfd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		ServiceDescription serviceDesc = new ServiceDescription();
		serviceDesc.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
		serviceDesc.addOntologies(FIPAManagementOntology.NAME);
		serviceDesc.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
		serviceDesc.setType("fipa-df");
		serviceDesc.setName("df-service");
		dfd.addServices(serviceDesc);
		return dfd;
	}

	/**
	 * Federates two agent platforms.
	 * 
	 * @param aidParentDF
	 *            The parent DF
	 * @param aidChildDF
	 *            The child DF
	 * @param a
	 *            Reference to an agent that will request the specified
	 *            federation. Ensure that a has already registered fipa-sl0
	 *            content language and {@link FIPAManagementOntology}
	 */
	public static ACLMessage requestDFFederation(AID aidParentDF,
			AID aidChildDF, Agent a) {

		DFAgentDescription dfd = getDFAgentDescription(aidChildDF);
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		ACLMessage response = null;

		Register r = new Register();
		r.setDescription(dfd);

		request.addReceiver(aidParentDF);
		request.setOntology(FIPAManagementOntology.getInstance().getName());
		request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action act = new Action(aidParentDF, r);

		try {
			a.getContentManager().fillContent(request, act);
			response = FIPAService.doFipaRequestClient(a, request, 5000);
			return response;
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	protected void afterMove() {	
		super.afterMove();
		manager = (ContentManager) getContentManager();
		codec = new SLCodec();
		ontology = BasicOntology.getInstance();
		manager.registerLanguage(codec);
		manager.registerOntology(ontology);
		// manager.registerOntology(FIPAManagementOntology.getInstance());
		m_aidMyself = getAID();
		
		
	}
	
}
