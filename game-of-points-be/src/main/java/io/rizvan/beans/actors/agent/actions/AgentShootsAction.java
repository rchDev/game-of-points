package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.facts.PlayerHPChangeFact;

public class AgentShootsAction implements AgentAction {
    private final ActionType actionType = ActionType.SHOOT;

    @Override
    public void apply(GameState gameState) {
        var agent = gameState.getAgent();
        var player = gameState.getPlayer();

        var damage = agent.shoot();
        if (agent.canReach(player) && damage > 0) {
            var playerHp = player.getHitPoints();
            player.setHitPoints(playerHp - damage);
            gameState.registerFact(new PlayerHPChangeFact(player.getPoints(), true));
        }
    }

    @Override
    public ActionType getType() {
        return actionType;
    }
}