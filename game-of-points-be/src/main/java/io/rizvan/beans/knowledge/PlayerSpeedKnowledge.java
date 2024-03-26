package io.rizvan.beans.knowledge;

public class PlayerSpeedKnowledge extends KnowledgeItem<Double> {
    private double speed;

    public PlayerSpeedKnowledge() {
        this.speed = 0.0;
    }

    public PlayerSpeedKnowledge(double speed) {
        this.speed = speed;
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
