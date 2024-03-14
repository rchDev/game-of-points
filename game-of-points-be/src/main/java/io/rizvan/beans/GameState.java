package io.rizvan.beans;

import io.rizvan.beans.actors.Agent;
import io.rizvan.beans.actors.GameEntity;
import io.rizvan.beans.actors.Player;
import io.rizvan.beans.playerActions.PlayerAction;
import io.rizvan.utils.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private Player player;
    private Agent agent;
    private Zone zone;
    private long lastAppliedServerTimestamp;
    private long lastAppliedClientTimestamp;
    private int time;
    private RandomNumberGenerator rng;

    public static final int RESOURCE_SIZE = 20;
    public static final int POINTS_PER_RESOURCE = 10;

    public static final int GAME_TIME = 60;

    private final List<ResourcePoint> resources = Collections.synchronizedList(new ArrayList<>());

    public GameState(Player player, Agent agent, int zoneWidth, int zoneHeight, int time, RandomNumberGenerator rng) {
        this.rng = rng;
        this.player = player;
        this.agent = agent;
        this.zone = new Zone(zoneWidth, zoneHeight);
        this.time = time;
        this.lastAppliedServerTimestamp = 0;
        this.lastAppliedClientTimestamp = 0;
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

    public void removeResource(String id) {
        synchronized (resources) {
            resources.removeIf(rp -> rp.getId().equals(id));
        }
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

    public long getLastAppliedServerTimestamp() {
        return lastAppliedServerTimestamp;
    }

    public long getLastAppliedClientTimestamp() {
        return lastAppliedClientTimestamp;
    }

    public void applyAction(PlayerAction action) {
        if (!action.isLegal(this)) {
            return;
        }

        var succeeded = action.apply(this);
        if (succeeded) {
            lastAppliedServerTimestamp = action.getServerTimestamp();
            lastAppliedClientTimestamp = action.getClientTimestamp();
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
