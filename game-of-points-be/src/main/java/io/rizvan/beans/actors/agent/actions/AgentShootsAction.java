package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.facts.PlayerHPChangeFact;

public class AgentShootsAction extends AgentAction {
    @Override
    public void apply(GameState gameState) {
        var agent = gameState.getAgent();
        var player = gameState.getPlayer();
        System.out.println("AgentShootsAction ran");
        var damage = agent.shoot();
        if (agent.canReach(player) && damage > 0) {
            var playerHp = player.getHitPoints();
            player.setHitPoints(playerHp - damage);
            gameState.registerFact(new PlayerHPChangeFact(player.getPoints(), true));
        }
    }
}