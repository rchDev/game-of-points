package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.facts.PlayerShootingFact;

public class PlayerShootingAction extends PlayerAction {
    private int damage;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public PlayerShootingAction(PlayerShootingAction other) {
        super(other);
        this.damage = other.damage;
    }

    @Override
    public boolean apply(GameState gameState) {
        var agent = gameState.getAgent();
        var player = gameState.getPlayer();

        int damage = player.shoot();
        if (player.canReach(agent) && damage > 0) {
            int agentHp = agent.getHitPoints();
            agent.setHitPoints(agentHp - damage);
            this.damage = damage;
        }

        return damage > 0;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return damage == gameState.getPlayer().getDamage();
    }
}
