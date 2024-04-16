package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerShootingAction extends PlayerAction {
    private int damage;

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
        var agent = gameState.getAgent();
        var player = gameState.getPlayer();

        System.out.println("gameStateTimeStamp: " + gameStateTimeStamp);

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
