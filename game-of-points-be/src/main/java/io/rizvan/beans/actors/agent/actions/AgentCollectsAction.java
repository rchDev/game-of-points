package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.ResourcePoint;

public class AgentCollectsAction implements AgentAction {
    private final ResourcePoint resource;
    private final ActionType actionType;

    public AgentCollectsAction(ResourcePoint resource) {
        this.resource = resource;
        this.actionType = ActionType.COLLECT;
    }

    @Override
    public ActionType getType() {
        return actionType;
    }

    @Override
    public void apply(GameState gameState) {
        var agent = gameState.getAgent();
        agent.setPoints(agent.getPoints() + resource.getPoints());
        gameState.removeResource(resource.getId());
    }
}
