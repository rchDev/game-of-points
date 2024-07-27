package io.rizvan.beans.actors.agent;

import io.rizvan.beans.GameState;
import io.rizvan.beans.knowledge.AgentKnowledge;
import io.rizvan.beans.knowledge.AgentPossibilities;
import jakarta.annotation.PreDestroy;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


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
    public void reason(GameState gameState) {
        knowledge.setPlayerHitBoxKnowledge(gameState.getPlayer().getHitBox(), true);
        KieSession kieSession = kieContainer.newKieSession("myKsession");
        kieSession.insert(gameState);
        kieSession.insert(gameState.getAgent());
        kieSession.insert(knowledge);
        kieSession.insert(possibilities);

        try {
            gameState.getFacts().forEach(kieSession::insert);
            gameState.clearFacts();

            kieSession.getAgenda().getAgendaGroup("inference-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("possibilities-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("agent-choices-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("agent-actions-group").setFocus();
            kieSession.fireAllRules();

            System.out.println("--------------------------");
        } finally {
            kieSession.dispose();
        }
    }

    @Override
    public AgentKnowledge getKnowledge() {
        return knowledge;
    }

    @PreDestroy
    public void cleanup() {
        if (kieContainer != null) {
            kieContainer.dispose();
        }
    }
}
