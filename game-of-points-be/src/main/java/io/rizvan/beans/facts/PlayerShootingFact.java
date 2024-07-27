package io.rizvan.beans.facts;

public class PlayerShootingFact implements Fact {
    private final int damage;
    private final boolean success;

    public PlayerShootingFact(int damage, boolean success) {
        this.damage = damage;
        this.success = success;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
