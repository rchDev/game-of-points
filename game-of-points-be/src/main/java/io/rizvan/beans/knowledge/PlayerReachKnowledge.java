package io.rizvan.beans.knowledge;

public class PlayerReachKnowledge extends KnowledgeItem<Double> {
    private Double reach;

    public PlayerReachKnowledge() {
        this.reach = 0.0;
    }

    public PlayerReachKnowledge(Double reach) {
        this.reach = reach;
    }

    @Override
    public Double getValue() {
        return reach;
    }

    @Override
    public void setValue(Double value) {
        this.reach = value;
    }
}
