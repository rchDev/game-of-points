package io.rizvan.beans;

import io.rizvan.beans.actors.GameEntity;

public class ResourcePoint extends GameEntity {
    private final HitBox hitbox;
    private int points;

    public ResourcePoint(double x, double y, double width, double height, int points) {
        super(x, y, width, height);
        this.points = points;
        this.hitbox = new HitBox(x, y, width, height);
    }

    public HitBox getHitbox() {
        return hitbox;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
