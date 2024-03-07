package io.rizvan.beans.facts;

public class PlayerMovedFact {
    private double mouseX;
    private double mouseY;
    private double playerX;
    private double playerY;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PlayerMoveFact{" +
                "mouseX=" + mouseX +
                ", mouseY=" + mouseY +
                ", playerX=" + playerX +
                ", playerY=" + playerY +
                ", timestamp=" + timestamp +
                '}';
    }
}
