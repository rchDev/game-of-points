package io.rizvan.beans;

import jakarta.inject.Inject;

import java.util.List;

public class AgentKnowledge {
    private double playerX;
    private double playerY;
    private double mouseX;
    private double mouseY;
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
        MOUSE_X("mouseX"),
        MOUSE_Y("mouseY"),
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

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
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

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setPlayerX(double playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(double playerY) {
        this.playerY = playerY;
    }

    public void setShotsHeard(int shotsHeard) {
        this.shotsHeard = shotsHeard;
    }

    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setPlayerDamage(int playerDamage) {
        this.playerDamage = playerDamage;
    }

    public void setPlayerHitPoints(int playerHitPoints) {
        this.playerHitPoints = playerHitPoints;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public void setResourcePointLocations(List<ResourcePoint> resourcePointLocations) {
        this.resourcePointLocations = resourcePointLocations;
    }

    public void setPossibleWeapons(WeaponCache possibleWeapons) {
        this.possibleWeapons = possibleWeapons;
    }

    @Override
    public String toString() {
        return "AgentKnowledge{" +
                "playerX=" + playerX +
                ", playerY=" + playerY +
                ", mouseX=" + mouseX +
                ", mouseY=" + mouseY +
                ", shotsHeard=" + shotsHeard +
                ", playerSpeed=" + playerSpeed +
                ", playerDamage=" + playerDamage +
                ", playerHitPoints=" + playerHitPoints +
                ", timeLeft=" + timeLeft +
                ", playerPoints=" + playerPoints +
                ", resourcePointLocations=" + resourcePointLocations +
                ", possibleWeapons=" + possibleWeapons +
                '}';
    }
}
