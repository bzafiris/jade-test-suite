package gr.aueb.jade.test.memkb;

import org.junit.Assert;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFMemKB;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.util.leap.List;
import test.common.Test;
import test.common.TestException;

public class DFMemKBTest extends Test /* implements BackendMethodCallback */ {

	@Override
	public Behaviour load(final Agent a) throws TestException {

		return new Behaviour(a) {

			@Override
			public boolean done() {
				return true;
			}

			@Override
			public void action() {
				// setup test
				DFMemKB kb = new DFMemKB(10);
				kb.setLeaseManager(new MockLeaseManager());
				DFAgentDescription desc1 = new DFAgentDescription();
				desc1.setName(new AID("a1@localhost:1099/JADE", true));
				desc1.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
				desc1.addOntologies(FIPANames.Ontology.SL0_ONTOLOGY);
				
				DFAgentDescription desc2 = new DFAgentDescription();
				desc2.setName(new AID("a2@localhost:1099/JADE", true));
				desc2.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
				desc2.addOntologies(FIPANames.Ontology.SL0_ONTOLOGY);
				
				DFAgentDescription desc3 = new DFAgentDescription();
				desc3.setName(new AID("a3@localhost:1099/JADE", true));
				desc3.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
				desc3.addOntologies(FIPANames.Ontology.SL1_ONTOLOGY);
				
				DFAgentDescription desc4 = new DFAgentDescription();
				desc4.setName(new AID("a3@localhost:1099/JADE", true));
				desc4.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
				desc4.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
				desc4.addOntologies(FIPANames.Ontology.SL0_ONTOLOGY);
				
				kb.register("a1", desc1);
				kb.register("a2", desc2);
				kb.register("a3", desc3);
				kb.register("a4", desc4);
				
				// execute test
				// find all descriptions that support the FIPA_SL language
				DFAgentDescription template1 = new DFAgentDescription();
				template1.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
				template1.setName(new AID("a3@localhost:1099/JADE", true));
				
				List results = kb.search(template1);
				
				if (results.size() == 2){
					passed("Test passed");
				} else {
					failed("DFMemKB test failed");
				}
				
			}

		};

	}
}
