package io.rizvan.beans.actors;

import io.rizvan.beans.Weapon;

public class Player extends CompetingEntity {
    public enum Type {
        SNIPER(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.SNIPER.get()),
        SOLDIER(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.CARBINE.get()),
        SCOUT(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.SUB_MACHINE.get()),
        SPEED_DEMON(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.PISTOL.get());

        private final Player player;

        Type(int hp, double x, double y, int width, int height, double speed, int points, Weapon weapon) {
            this.player = new Player(hp, x, y, width, height, speed, points, weapon);
        }

        public Player get() {
            return player;
        }
    }

    public Player(int hitPoints, double x, double y, int width, int height, double speed, int points, Weapon weapon) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
    }

    public static Player fromWeapon(Weapon weapon) {
        return switch (weapon.getName()) {
            case "Sniper" -> Type.SNIPER.get();
            case "Sub-machine" -> Type.SCOUT.get();
            case "Pistol" -> Type.SPEED_DEMON.get();
            default -> Type.SOLDIER.get();
        };
    }
}
