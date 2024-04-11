package io.rizvan.beans.facts;

public class PlayerMovementFact implements Fact {
    private final double x;
    private final double y;
    private final boolean success;

    public PlayerMovementFact(double x, double y, boolean success) {
        this.x = x;
        this.y = y;
        this.success = success;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
