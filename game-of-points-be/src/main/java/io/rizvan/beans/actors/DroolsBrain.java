package io.rizvan.beans.actors;

import io.rizvan.beans.KnowledgeInferredSignal;
import io.rizvan.beans.knowledge.AgentKnowledge;
import io.rizvan.beans.KnowledgeUpdateSignal;
import io.rizvan.beans.facts.Fact;
import io.rizvan.beans.knowledge.AgentPossibilities;
import jakarta.annotation.PreDestroy;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;

public class DroolsBrain implements AgentsBrain {

    private final AgentKnowledge knowledge;
    private final AgentPossibilities possibilities;
    // I want KieSession to reside here
    private final KieContainer kieContainer;

    public DroolsBrain() {
        knowledge = new AgentKnowledge();
        possibilities = new AgentPossibilities();
        KieServices kieService = KieServices.Factory.get();
        kieContainer = kieService.getKieClasspathContainer();
    }

    @Override
    public void reason(List<Fact> facts, Agent agent) {
        KieSession kieSession = kieContainer.newKieSession("myKsession");
        kieSession.insert(agent);
        kieSession.insert(knowledge);
        kieSession.insert(possibilities);
        kieSession.insert(new KnowledgeUpdateSignal());

        try {
            facts.forEach(kieSession::insert);

            kieSession.getAgenda().getAgendaGroup("inference-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("possibilities-group").setFocus();
            kieSession.fireAllRules();

//            System.out.println(knowledge);
        } finally {
            kieSession.dispose();
        }
    }

    @PreDestroy
    public void cleanup() {
        if (kieContainer != null) {
            kieContainer.dispose();
        }
    }
}
