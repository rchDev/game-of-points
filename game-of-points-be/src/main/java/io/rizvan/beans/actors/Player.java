package io.rizvan.beans.actors;

import io.rizvan.beans.Weapon;
import io.rizvan.beans.actors.player.PlayerAnswers;

public class Player extends CompetingEntity {

    private final PlayerAnswers playerAnswers;

    public Player(int hitPoints, double x, double y, int width, int height, double speed, int points, Weapon weapon, PlayerAnswers playerAnswers) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
        this.playerAnswers = playerAnswers;
    }

    public Player(Player other) {
        super(other);
        this.playerAnswers = other.playerAnswers;
    }

    public static Player fromWeapon(Weapon weapon, PlayerAnswers playerAnswers) {
        return new Player(3, 0, 0, 50, 50, 1.0, 0, weapon, playerAnswers);
    }

    public PlayerAnswers getPlayerAnswers() {
        return playerAnswers;
    }
}
