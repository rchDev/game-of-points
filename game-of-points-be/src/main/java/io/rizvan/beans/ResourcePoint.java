package io.rizvan.beans;

import io.rizvan.beans.actors.GameEntity;

public class ResourcePoint extends GameEntity {
    private int points;

    public ResourcePoint(double x, double y, int width, int height, int points) {
        super(x, y, width, height);
        this.points = points;
    }

    public ResourcePoint(ResourcePoint other) {
        super(other);
        this.points = other.points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
