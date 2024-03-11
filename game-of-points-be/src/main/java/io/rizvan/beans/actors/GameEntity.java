package io.rizvan.beans.actors;

import io.rizvan.beans.HitBox;

public abstract class GameEntity {
    protected double x;
    protected double y;
    protected HitBox hitBox;

    public GameEntity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.hitBox = new HitBox(width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }
}
