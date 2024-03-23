package io.rizvan.beans.actors;

import io.rizvan.beans.AgentKnowledge;
import io.rizvan.beans.ResourcePoint;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DroolsBrain implements AgentsBrain {


    private final AgentKnowledge knowledge;
    // I want KieSession to reside here

    public DroolsBrain() {
        knowledge = new AgentKnowledge();
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
    public void reason(Agent agent) {

    }
}
