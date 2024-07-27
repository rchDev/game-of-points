package io.rizvan.beans.facts;

public class GameTimeChangeFact implements Fact {
    private final int timeLeft;
    private final boolean success;

    public GameTimeChangeFact(int timeLeft, boolean success) {
        this.timeLeft = timeLeft;
        this.success = success;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
