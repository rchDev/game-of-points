package io.rizvan.beans.facts;

public class PlayerHPChangeFact implements Fact {
    private int hp;

    public PlayerHPChangeFact(int hp) {
        this.hp = hp;
    }

    public int getHP() {
        return hp;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }
}
