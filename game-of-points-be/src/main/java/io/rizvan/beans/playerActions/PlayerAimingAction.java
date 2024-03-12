package io.rizvan.beans.playerActions;

import io.rizvan.beans.actors.CompetingEntity;

public class PlayerAimingAction extends PlayerAction {
    private double mouseX;
    private double mouseY;

    @Override
    public boolean check(CompetingEntity entity1, CompetingEntity entity2) {
        return false;
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
}
