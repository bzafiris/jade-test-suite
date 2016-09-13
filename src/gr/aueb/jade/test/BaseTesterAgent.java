package gr.aueb.jade.test;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import test.common.TestException;
import test.common.TestGroup;
import test.common.TestUtility;
import test.common.TesterAgent;
import test.common.testerAgentControlOntology.TesterAgentControlOntology;

public class BaseTesterAgent extends TesterAgent {

	public static final String BASE_TEST_PACKAGE = "gr//aueb//jade//test//";
	public static final String BASE_RESOURCES_DIR = "resources//test//";
	
	public static final String MAIN_CONTAINER_NAME = "Main-Container";
	
	@Override
	protected void setup() {
		super.setup();

		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(
				TesterAgentControlOntology.getInstance());
		
		getContentManager().registerLanguage(new SLCodec(0));
		getContentManager().registerOntology(FIPAManagementOntology.getInstance());

	}
	
	@Override
	protected TestGroup getTestGroup() {
		return null;
	}


	protected void killAgent(String localName){
		
		try {
			TestUtility.killAgent(this, new AID(localName, false));
		} catch (TestException e) {
			e.printStackTrace();
		}
		
	}
	
}
