package io.rizvan.beans.actors;

import io.rizvan.beans.RangedWeapon;

public class CompetingEntity extends GameEntity {
    private RangedWeapon weapon;
    private int hitPoints;
    private double speed;
    private int points;

    private double mouseX;
    private double mouseY;

    public static final double BASE_SPEED = 0.4;

    public CompetingEntity() {}

    public CompetingEntity(int hitPoints, double x, double y, int width, int height, double speed, int points, RangedWeapon weapon) {
        super(x, y, width, height);
        this.weapon = weapon;
        this.hitPoints = hitPoints;
        this.speed = speed * BASE_SPEED;
        this.points = points;
        this.mouseX = 0.0;
        this.mouseY = 0.0;
    }

    public double getReach() {
        return weapon.getRange();
    }

    public double getSpeed() {
        return speed * weapon.getSpeedModifier();
    }

    public int getDamage() {
        return weapon.getDamage();
    }

    public int getAmmo() {
        return weapon.getAmmo();
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

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setWeapon(RangedWeapon weapon) {
        this.weapon = weapon;
    }
}
