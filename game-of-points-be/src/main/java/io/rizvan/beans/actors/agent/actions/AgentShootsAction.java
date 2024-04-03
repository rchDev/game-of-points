package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;

public class AgentShootsAction extends AgentAction {
    @Override
    public void apply(GameState gameState) {
        var damage = gameState.getAgent().shoot();

        var playerX = gameState.getPlayer().getX();
        var playerY = gameState.getPlayer().getY();
        var playerHitBox = gameState.getPlayer().getHitBox();
    }
}
