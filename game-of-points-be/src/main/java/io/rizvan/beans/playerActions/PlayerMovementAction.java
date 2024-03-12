package io.rizvan.beans.playerActions;

import io.rizvan.beans.actors.CompetingEntity;

public class PlayerMovementAction extends PlayerAction {
    private double dx;
    private double dy;

    @Override
    public boolean check(CompetingEntity entity1, CompetingEntity entity2) {
        return true;
    }

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
}
