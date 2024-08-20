package io.rizvan.beans.knowledge;

import io.rizvan.utils.Coord;

public class PlayerAimKnowledge extends KnowledgeItem<Coord<Double>> {

    private Coord<Double> value;

    public PlayerAimKnowledge() {
        value = new Coord<>(0.0, 0.0);
        setName("player_aim");
    }

    public PlayerAimKnowledge(double x, double y) {
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
