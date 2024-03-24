package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerShootingAction extends PlayerAction {
    private double mouseX;
    private double mouseY;
    private int damage;

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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
