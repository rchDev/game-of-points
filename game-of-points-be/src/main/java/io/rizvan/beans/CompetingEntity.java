package io.rizvan.beans;

public class CompetingEntity extends GameEntity {
    private RangedWeapon weapon;

    public CompetingEntity(int hitPoints, double x, double y, double speed, double points) {
        super(hitPoints, x, y, speed, points);
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
        return ;
    }
}
