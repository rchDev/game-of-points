package io.rizvan.beans.Facts;

import io.rizvan.beans.actors.AgentsBrain;

public class GameTimeChangeFact implements Fact {
    private int timeLeft;

    public GameTimeChangeFact(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    public void supplyInfo(AgentsBrain brain) {
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
