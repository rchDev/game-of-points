package io.rizvan.beans.Facts;

import io.rizvan.beans.actors.AgentsBrain;

public class PlayerMovementFact implements Fact {

    private final double x;
    private final double y;

    public PlayerMovementFact(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void supplyInfo(AgentsBrain brain) {
        brain.senseMovement(x, y);
    }
}
