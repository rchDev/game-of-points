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
        System.out.println("collecting...");
        System.out.println("resources before:" + gameState.getResources().size());
        ResourcePoint rp = gameState.removeResource(resource.getId());
        System.out.println("resources after:" + gameState.getResources().size());
        if (rp != null) {
            agent.setPoints(agent.getPoints() + rp.getPoints());
        }
    }
}
