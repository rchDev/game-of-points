package io.rizvan.beans.Facts;

import io.rizvan.beans.actors.AgentsBrain;

public class PlayerCollectionFact implements Fact {
    private int totalPoints;

    public PlayerCollectionFact(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public void supplyInfo(AgentsBrain brain) {

    }
}
