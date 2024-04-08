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
        var entityEdges = calculateEdges(this);
        var otherEdges = calculateEdges(x, y, hitBox);

        var entityLeftReach = entityEdges.left - getReach();
        var entityRightReach = entityEdges.right +getReach();
        var entityTopReach = entityEdges.top - getReach();
        var entityBottomReach = entityEdges.bottom + getReach();

        boolean horizontalOverlap = entityRightReach >= otherEdges.left && entityLeftReach <= otherEdges.right;
        boolean verticalOverlap = entityBottomReach >= otherEdges.top && entityTopReach <= otherEdges.bottom;

        return horizontalOverlap && verticalOverlap;
    }

    private EntityEdges calculateEdges(CompetingEntity entity) {
        return calculateEdges(entity.getX(), entity.getY(), entity.getHitBox());
    }

    private EntityEdges calculateEdges(double x, double y, HitBox hitBox) {
        double left = x - hitBox.getWidth() / 2.0;
        double right = x + hitBox.getWidth() / 2.0;
        double top = y - hitBox.getHeight() / 2.0;
        double bottom = y + hitBox.getHeight() / 2.0;

        return new EntityEdges(left, right, top, bottom);
    }

    private class EntityEdges {
        double left, right, top, bottom;

        EntityEdges(double left, double right, double top, double bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }
}
