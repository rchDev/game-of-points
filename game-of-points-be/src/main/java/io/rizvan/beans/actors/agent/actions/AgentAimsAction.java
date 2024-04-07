package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;

public class AgentAimsAction extends AgentAction {
    @Override
    public void apply(GameState gameState) {
        var player = gameState.getPlayer();
        var agent = gameState.getAgent();

        agent.setMouseX(player.getX());
        agent.setMouseY(player.getY());
    }
}
