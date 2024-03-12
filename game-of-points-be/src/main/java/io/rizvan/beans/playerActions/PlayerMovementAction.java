package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerMovementAction extends PlayerAction {
    private double dx;
    private double dy;

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public boolean apply(GameState gameState) {

        return true;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return false;
    }
}
