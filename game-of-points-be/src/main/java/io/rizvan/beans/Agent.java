package io.rizvan.beans;

public class Agent extends CompetingEntity {
    private PlayerInfo playerInfo;

    public Agent(int hitPoints, double x, double y, double speed, double points) {
        super(hitPoints, x, y, speed, points);
    }
}
