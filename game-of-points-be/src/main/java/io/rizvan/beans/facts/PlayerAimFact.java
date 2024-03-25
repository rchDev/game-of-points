package io.rizvan.beans.facts;

import io.rizvan.beans.actors.AgentsBrain;

public class PlayerAimFact implements Fact {
    double mouseX;
    double mouseY;

    public PlayerAimFact(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
}
