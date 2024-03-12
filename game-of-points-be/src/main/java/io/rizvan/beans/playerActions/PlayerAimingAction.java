package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerAimingAction extends PlayerAction {
    private double mouseX;
    private double mouseY;

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

    @Override
    public boolean apply(GameState gameState) {
        return false;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return false;
    }
}
