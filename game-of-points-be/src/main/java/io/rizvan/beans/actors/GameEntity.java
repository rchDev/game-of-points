package io.rizvan.beans.actors;

import io.rizvan.beans.HitBox;

public abstract class GameEntity {
    protected int hitPoints;
    protected double x;
    protected double y;
    protected double speed;
    protected int points;

    protected HitBox hitBox;

    public GameEntity(int hitPoints, double x, double y, double width, double height, double speed, int points) {
        this.hitPoints = hitPoints;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.points = points;
        this.hitBox = new HitBox(x, y, width, height);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }
}
