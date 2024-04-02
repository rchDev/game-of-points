package io.rizvan.beans.facts;

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
}
