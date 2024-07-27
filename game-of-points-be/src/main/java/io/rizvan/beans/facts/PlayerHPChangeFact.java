package io.rizvan.beans.facts;

public class PlayerHPChangeFact implements Fact {
    private final int hp;
    private final boolean success;

    public PlayerHPChangeFact(int hp, boolean success) {
        this.hp = hp;
        this.success = success;
    }

    public int getHP() {
        return hp;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
