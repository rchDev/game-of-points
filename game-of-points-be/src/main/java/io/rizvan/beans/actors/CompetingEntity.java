package io.rizvan.beans.actors;

import io.rizvan.beans.HitBox;
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
        this.hitPoints = Math.max(hitPoints, 0);
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

    public int shoot() {
        return weapon.shoot();
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }

    public boolean canReach(CompetingEntity other) {
        return canReach(other.getX(), other.getY(), other.getHitBox());
    }

    public boolean canReach(double x, double y, HitBox hitBox) {
        double circleCenterX = this.getX();
        double circleCenterY = this.getY();
        double circleRadius = this.getReach();

        double halfWidth = hitBox.getWidth() / 2.0;
        double halfHeight = hitBox.getHeight() / 2.0;

        double corner1X = x - halfWidth;
        double corner1Y = y - halfHeight;
        double corner2X = x + halfWidth;
        double corner2Y = y - halfHeight;
        double corner3X = x + halfWidth;
        double corner3Y = y + halfHeight;
        double corner4X = x - halfWidth;
        double corner4Y = y + halfHeight;

        // Check if all corners are within the circle's reach
        return isPointWithinCircle(corner1X, corner1Y, circleCenterX, circleCenterY, circleRadius) &&
                isPointWithinCircle(corner2X, corner2Y, circleCenterX, circleCenterY, circleRadius) &&
                isPointWithinCircle(corner3X, corner3Y, circleCenterX, circleCenterY, circleRadius) &&
                isPointWithinCircle(corner4X, corner4Y, circleCenterX, circleCenterY, circleRadius);
    }

    private boolean isPointWithinCircle(double pointX, double pointY, double centerX, double centerY, double radius) {
        // Calculate the distance from the point to the circle's center
        double distanceSquared = (pointX - centerX) * (pointX - centerX) + (pointY - centerY) * (pointY - centerY);
        // Check if the distance is less than or equal to the radius
        return distanceSquared <= radius * radius;
    }
}
