package io.rizvan.beans.facts;

public class PlayerCollectionFact implements Fact {
    private final int totalPoints;
    private final boolean success;

    public PlayerCollectionFact(int totalPoints, boolean success) {
        this.totalPoints = totalPoints;
        this.success = success;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
