package io.rizvan.beans.facts;

import io.rizvan.beans.actors.AgentsBrain;

public class PlayerShootingFact implements Fact {
    private final int damage;

    @Override
    public void supplyInfo(AgentsBrain brain) {
        brain.senseShot(damage);
    }

    public PlayerShootingFact(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
