package io.rizvan.beans;

import io.rizvan.beans.actors.Agent;
import io.rizvan.beans.actors.CompetingEntity;
import io.rizvan.beans.actors.GameEntity;
import io.rizvan.beans.actors.Player;
import io.rizvan.utils.RandomNumberGenerator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private Player player;
    private Agent agent;
    private Zone zone;

    private RandomNumberGenerator rng;

    public static final int RESOURCE_SIZE = 20;
    public static final int POINTS_PER_RESOURCE = 10;

    private final List<ResourcePoint> resources = new CopyOnWriteArrayList<>();

    public GameState(Player player, Agent agent, int zoneWidth, int zoneHeight, RandomNumberGenerator rng) {
        this.rng = rng;
        this.player = player;
        this.agent = agent;
        this.zone = new Zone(zoneWidth, zoneHeight);
        setRandomPosition(this.player);
        setRandomPosition(this.agent);
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(int width, int height) {
        this.zone.setWidth(width);
        this.zone.setHeight(height);
    }

    public void setZone(Zone zone) {
        this.zone = zone;
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

    private void setRandomPosition(GameEntity entity) {
        var minX = entity.getHitBox().getWidth() / 2 - 1;
        var maxX = zone.getWidth() - entity.getHitBox().getWidth() / 2 - 1;

        var minY = entity.getHitBox().getHeight() / 2 - 1;
        var maxY = zone.getHeight() - entity.getHitBox().getHeight() / 2 - 1;

        var x = rng.getInteger(minX, maxX);
        var y = rng.getInteger(minY, maxY);

        entity.setX(x);
        entity.setY(y);
    }
}
