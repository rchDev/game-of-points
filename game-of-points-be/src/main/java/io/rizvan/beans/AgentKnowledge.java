package io.rizvan.beans;

import jakarta.inject.Inject;

import java.util.List;

public class AgentKnowledge {
    private double playerX;
    private double playerY;
    private int shotsHeard;
    private double playerSpeed;
    private int playerDamage;
    private int playerHitPoints;
    private int timeLeft;
    private int playerPoints;
    private List<ResourcePoint> resourcePointLocations;

    @Inject
    public WeaponCache possibleWeapons;

    public AgentKnowledge() {
        this.playerX = 0.0;
        this.playerY = 0.0;
        this.shotsHeard = 0;
        this.playerDamage = 0;
        this.playerSpeed = 0;
        this.timeLeft = GameState.GAME_TIME;
        playerHitPoints = 3;
    }

    public enum Type {
        HP("hp"),
        PLAYER_X("playerX"),
        PLAYER_Y("playerY"),
        SHOT_COUNT("shotCount"),
        SPEED("speed"),
        PLAYER_DAMAGE("damage"),
        PLAYER_POINTS("playerPoints"),
        GAME_TIME("gameTime"),
        RESOURCE_POINTS("resourcePoints");

        private final String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public int getShotsHeard() {
        return shotsHeard;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getPlayerDamage() {
        return playerDamage;
    }

    public int getPlayerHitPoints() {
        return playerHitPoints;
    }

    public void update(Type type, Object value) {
        if (type == Type.HP ||
                type == Type.PLAYER_DAMAGE ||
                type == Type.SHOT_COUNT ||
                type == Type.GAME_TIME ||
                type == Type.PLAYER_POINTS && !(value instanceof Integer)
        ) {
            throw new IllegalArgumentException("provided value is not of type: Integer");
        }

        if (type == Type.PLAYER_X || type == Type.PLAYER_Y || type == Type.SPEED && !(value instanceof Double)) {
            throw new IllegalArgumentException("provided value is not of type: Double");
        }

        switch (type.getLabel()) {
            case "hp" -> playerHitPoints = (int) value;
            case "playerX" -> playerX = (double) value;
            case "playerY" -> playerY = (double) value;
            case "shotCount" -> shotsHeard = (int) value;
            case "speed" -> playerSpeed = (double) value;
            case "damage" -> playerDamage = (int) value;
            case "gameTime" -> timeLeft = (int) value;
            case "playerPoints" -> playerPoints = (int) value;
            case "resourcePoints" -> resourcePointLocations = (List<ResourcePoint>) value;
            default -> {
                // do nothing
            }
        }
    }
}
