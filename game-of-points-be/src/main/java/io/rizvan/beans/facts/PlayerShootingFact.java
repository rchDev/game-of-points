package io.rizvan.beans.facts;

public class PlayerShootingFact implements Fact {
    private final int damage;

    public PlayerShootingFact(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
