package io.rizvan.beans.actors;

import io.rizvan.beans.RangedWeapon;

public class CompetingEntity extends GameEntity {
    private final RangedWeapon weapon;

    public CompetingEntity(int hitPoints, double x, double y, double speed, int points, RangedWeapon weapon) {
        super(hitPoints, x, y, speed, points);
        this.weapon = weapon;
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
