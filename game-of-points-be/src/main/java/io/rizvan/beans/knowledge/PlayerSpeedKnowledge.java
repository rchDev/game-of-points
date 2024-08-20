package io.rizvan.beans.knowledge;

import io.rizvan.beans.Weapon;

public class PlayerSpeedKnowledge extends KnowledgeItem<Double> {
    private double speed;

    public PlayerSpeedKnowledge() {
        this.speed = 0.0;
        setName(Weapon.Stat.SPEED_MOD.getName());
    }

    public PlayerSpeedKnowledge(double speed) {
        this.speed = speed;
        setName(Weapon.Stat.SPEED_MOD.getName());
    }

    @Override
    public Double getValue() {
        return speed;
    }

    @Override
    public void setValue(Double value) {
        this.speed = value;
    }
}
