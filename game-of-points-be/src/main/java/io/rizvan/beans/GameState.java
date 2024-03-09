package io.rizvan.beans;

import io.rizvan.beans.actors.Agent;
import io.rizvan.beans.actors.CompetingEntity;
import io.rizvan.beans.actors.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private Player player;
    private Agent agent;

    private static final int RESOURCE_SIZE = 10;
    private static final int POINTS_PER_RESOURCE = 10;

    private List<ResourcePoint> resources = new CopyOnWriteArrayList<>();

    public GameState(Player player, Agent agent) {
        this.player = player;
        this.agent = agent;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void addResource(double x, double y) {
        resources.add(new ResourcePoint(x, y, RESOURCE_SIZE, RESOURCE_SIZE, POINTS_PER_RESOURCE));
    }

    public List<ResourcePoint> getResources() {
        return resources;
    }

    public void collectResource(double x, double y, CompetingEntity entity) {
        //TODO: implement this stuff
    }
}
