package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerShootingAction extends PlayerAction {
    private int damage;

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public PlayerShootingAction() {}

    public PlayerShootingAction(PlayerShootingAction other) {
        super(other);
        this.damage = other.damage;
    }

    @Override
    public boolean apply(GameState gameState) {
        var agentSeenByPlayer = playerGameState.getAgent();
        var currentAgent = gameState.getAgent();
        var player = gameState.getPlayer();

        int damage = player.shoot();
        if (player.canReach(agentSeenByPlayer) && damage > 0) {
            int agentHp = currentAgent.getHitPoints();
            currentAgent.setHitPoints(agentHp - damage);
            this.damage = damage;
        }

        return damage > 0;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return damage == gameState.getPlayer().getDamage();
    }
}
