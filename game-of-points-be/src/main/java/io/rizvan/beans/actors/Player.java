package io.rizvan.beans.actors;

import io.rizvan.beans.RangedWeapon;

public class Player extends CompetingEntity {
    public Player(int hitPoints, double x, double y, double speed, int points, RangedWeapon weapon) {
        super(hitPoints, x, y, speed, points, weapon);
    }
}
