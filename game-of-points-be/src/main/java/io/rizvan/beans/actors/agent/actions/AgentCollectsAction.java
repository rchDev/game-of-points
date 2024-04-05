package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.ResourcePoint;

public class AgentCollectsAction extends AgentAction {
    private final ResourcePoint resource;

    public AgentCollectsAction(ResourcePoint resource) {
        this.resource = resource;
    }

    @Override
    public void apply(GameState gameState) {
        var agent = gameState.getAgent();
        agent.setPoints(agent.getPoints() + resource.getPoints());
        gameState.removeResource(resource.getId());
    }
}
