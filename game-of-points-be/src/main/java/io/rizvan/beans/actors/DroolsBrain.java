package io.rizvan.beans.actors;

import io.rizvan.beans.AgentKnowledge;
import io.rizvan.beans.Facts.Fact;
import io.rizvan.beans.ResourcePoint;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;

@ApplicationScoped
public class DroolsBrain implements AgentsBrain {

    private final AgentKnowledge knowledge;
    // I want KieSession to reside here
    private KieContainer kieContainer;


    public DroolsBrain() {
        knowledge = new AgentKnowledge();
    }

    @PostConstruct
    public void init() {
        // Set up the KieServices and get the KieContainer from the classpath
        KieServices kieService = KieServices.Factory.get();
        kieContainer = kieService.getKieClasspathContainer();
    }

    @Override
    public void senseShot(int damage) {
        knowledge.update(AgentKnowledge.Type.PLAYER_DAMAGE, damage);
    }

    @Override
    public void senseTime(int gameTimeLeft) {
        knowledge.update(AgentKnowledge.Type.GAME_TIME, gameTimeLeft);
    }

    @Override
    public void senseMovement(double x, double y) {
        knowledge.update(AgentKnowledge.Type.PLAYER_X, x);
        knowledge.update(AgentKnowledge.Type.PLAYER_Y, y);
    }

    @Override
    public void senseResourceCollection(int pointsCollected) {
        knowledge.update(AgentKnowledge.Type.PLAYER_POINTS, pointsCollected);
    }

    @Override
    public void senseResourceChange(List<ResourcePoint> resources) {
        knowledge.update(AgentKnowledge.Type.RESOURCE_POINTS, resources);
    }

    @Override
    public void reason(List<Fact> facts, Agent agent) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(agent);

        try {
            facts.forEach(kieSession::insert);
            kieSession.fireAllRules();
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
