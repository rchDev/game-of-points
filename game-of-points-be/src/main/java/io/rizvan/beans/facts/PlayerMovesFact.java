package io.rizvan.beans.facts;

import io.rizvan.beans.actors.CompetingEntity;

public class PlayerMovesFact extends Fact {
    private double mouseX;
    private double mouseY;
    private double playerX;
    private double playerY;

    @Override
    public String toString() {
        return "PlayerMovedFact{" +
                "mouseX=" + mouseX +
                ", mouseY=" + mouseY +
                ", playerX=" + playerX +
                ", playerY=" + playerY +
                ", timestamp=" + timestamp +
                '}';
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

    public double getPlayerX() {
        return playerX;
    }

    public void setPlayerX(double playerX) {
        this.playerX = playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public void setPlayerY(double playerY) {
        this.playerY = playerY;
    }

    @Override
    public boolean check(CompetingEntity entity1, CompetingEntity entity2) {
        return true;
    }
}
