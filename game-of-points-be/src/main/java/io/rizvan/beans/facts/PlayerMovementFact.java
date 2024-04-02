package io.rizvan.beans.facts;

public class PlayerMovementFact implements Fact {

    private final double x;
    private final double y;

    public PlayerMovementFact(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
