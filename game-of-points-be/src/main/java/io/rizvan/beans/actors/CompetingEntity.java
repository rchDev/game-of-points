package io.rizvan.beans.actors;

import io.rizvan.beans.RangedWeapon;

public class CompetingEntity extends GameEntity {
    private final RangedWeapon weapon;
    private int hitPoints;
    private double speed;
    private int points;

    public CompetingEntity(int hitPoints, double x, double y, double width, double height, double speed, int points, RangedWeapon weapon) {
        super(x, y, width, height);
        this.weapon = weapon;
        this.hitPoints = hitPoints;
        this.speed = speed;
        this.points = points;
    }

    public double getReach() {
        return weapon.getRange();
    }

    public double getSpeed() {
        return speed * weapon.getSpeedModifier();
    }

    public double getDamage() {
        return weapon.getDamage();
    }

    public void dealDamage(GameEntity entity) {
        return;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
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

    public void addPoints(int points) {
        this.points += points;
    }
}
