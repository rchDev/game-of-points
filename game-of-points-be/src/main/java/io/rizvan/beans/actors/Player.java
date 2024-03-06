package io.rizvan.beans.actors;

import io.rizvan.beans.RangedWeapon;

public class Player extends CompetingEntity {
    public Player(int hitPoints, double x, double y, double width, double height, double speed, int points, RangedWeapon weapon) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
    }
}
