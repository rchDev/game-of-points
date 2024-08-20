package io.rizvan.beans.knowledge;

import io.rizvan.utils.Coord;

public class PlayerPositionKnowledge extends KnowledgeItem<Coord<Double>> {
    private Coord<Double> value;

    public PlayerPositionKnowledge() {
        value = new Coord<>(0.0, 0.0);
        setName("position");
    }

    public PlayerPositionKnowledge(Double x, Double y) {
        value = new Coord<>(x, y);
    }

    @Override
    public Coord<Double> getValue() {
        return value;
    }

    @Override
    public void setValue(Coord<Double> value) {
        this.value = value;
    }
}
