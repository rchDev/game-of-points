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

    public PlayerAimingAction() {
    }

    public PlayerAimingAction(PlayerAimingAction other) {
        super(other);
        this.mouseX = other.mouseX;
        this.mouseY = other.mouseY;
    }

    @Override
    public boolean apply(GameState gameState) {
        gameState.getPlayer().setMouseX(mouseX);
        gameState.getPlayer().setMouseY(mouseY);

        return true;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return true;
    }
}
