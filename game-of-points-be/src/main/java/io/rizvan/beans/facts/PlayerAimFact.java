package io.rizvan.beans.facts;

public class PlayerAimFact implements Fact {
    private final double mouseX;
    private final double mouseY;
    private final boolean success;

    public PlayerAimFact(double mouseX, double mouseY, boolean success) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.success = success;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
