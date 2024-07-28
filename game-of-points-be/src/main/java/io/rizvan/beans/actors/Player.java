package io.rizvan.beans.actors;

import io.rizvan.beans.Weapon;

public class Player extends CompetingEntity {


    public Player(int hitPoints, double x, double y, int width, int height, double speed, int points, Weapon weapon) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
    }

    public Player(Player other) {
        super(other);
    }

    public static Player fromWeapon(Weapon weapon) {
        return new Player(3, 0, 0, 50, 50, 1.0, 0, weapon);
    }
}
