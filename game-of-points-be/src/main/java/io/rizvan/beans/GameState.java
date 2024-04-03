package io.rizvan.beans;

import io.rizvan.beans.actors.agent.actions.AgentAction;
import io.rizvan.beans.facts.*;
import io.rizvan.beans.actors.agent.Agent;
import io.rizvan.beans.actors.GameEntity;
import io.rizvan.beans.actors.Player;
import io.rizvan.beans.playerActions.*;
import io.rizvan.utils.RandomNumberGenerator;
import jakarta.json.bind.annotation.JsonbTransient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameState {
    private Player player;
    private Agent agent;
    private Zone zone;
    private int time;
    private long lastUpdateTime;
    private long deltaBetweenUpdates;
    private final RandomNumberGenerator rng;

    public static final int RESOURCE_SIZE = 20;
    public static final int POINTS_PER_RESOURCE = 10;
    public static final int GAME_TIME = 60;

    private final List<ResourcePoint> resources = Collections.synchronizedList(new ArrayList<>());

    private PlayerMovementAction lastAppliedPlayerMovement;
    private PlayerAimingAction lastAppliedPlayerAim = null;
    private PlayerCollectingAction lastAppliedPlayerCollection = null;
    private PlayerShootingAction lastAppliedPlayerShot = null;

    private final FactStorage factStorage;

    public GameState(Player player, Agent agent, int zoneWidth, int zoneHeight, int time, RandomNumberGenerator rng) {
        this.rng = rng;
        this.player = player;
        this.agent = agent;
        this.lastUpdateTime = 0;
        this.deltaBetweenUpdates = 0;
        this.zone = new Zone(zoneWidth, zoneHeight);
        this.time = time;
        this.factStorage = new FactStorage();

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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getDeltaBetweenUpdates() {
        return deltaBetweenUpdates;
    }

    public void setDeltaBetweenUpdates(long delta) {
        this.deltaBetweenUpdates = delta;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void addResource(double x, double y) {
        resources.add(new ResourcePoint(x, y, RESOURCE_SIZE, RESOURCE_SIZE, POINTS_PER_RESOURCE));
        factStorage.add(new ResourcesChangeFact(resources));
    }

    public List<Fact> getFacts() {
        return factStorage.getAll();
    }

    public void clearFacts() {
        factStorage.clear();
    }

    public List<ResourcePoint> getResources() {
        return resources;
    }

    public void removeResource(String id) {
        synchronized (resources) {
            resources.removeIf(rp -> rp.getId().equals(id));
            factStorage.add(new ResourcesChangeFact(resources));
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

    public void applyAction(PlayerAction action) {
        if (!action.isLegal(this)) {
            return;
        }

        var succeeded = action.apply(this);
        if (succeeded) {
            registerAppliedAction(action);
            registerFact(action, true);
        } else if (action instanceof PlayerShootingAction) {
            registerFact(action, false);
        }
    }

    public void applyAction(AgentAction action) {
        action.apply(this);
    }

    private void registerAppliedAction(PlayerAction action) {
        switch (action.getType()) {
            case "shoot" -> lastAppliedPlayerShot = (PlayerShootingAction) action;
            case "move" -> lastAppliedPlayerMovement = (PlayerMovementAction) action;
            case "collect" -> lastAppliedPlayerCollection = (PlayerCollectingAction) action;
            case "aim" -> lastAppliedPlayerAim = (PlayerAimingAction) action;
        }
    }

    private void registerFact(PlayerAction action, boolean success) {
        switch (action.getType()) {
            case "shoot" -> {
                var damage = success ? ((PlayerShootingAction) action).getDamage() : 0;
                factStorage.add(new PlayerShootingFact(damage));
            }
            case "aim" -> {
                var aimingAction = (PlayerAimingAction) action;
                var mouseX = aimingAction.getMouseX();
                var mouseY = aimingAction.getMouseY();
                factStorage.add(new PlayerAimFact(mouseX, mouseY));
            }
            case "move" -> factStorage.add(new PlayerMovementFact(player.getX(), player.getY()));
            case "collect" -> factStorage.add(new PlayerCollectionFact(player.getPoints()));
            default -> {
                // do nothing
            }
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        factStorage.add(new GameTimeChangeFact(time));
    }

    @JsonbTransient
    public Optional<PlayerMovementAction> getLastAppliedPlayerMovement() {
        return Optional.ofNullable(lastAppliedPlayerMovement);
    }

    @JsonbTransient
    public Optional<PlayerAimingAction> getLastAppliedPlayerAim() {
        return Optional.ofNullable(lastAppliedPlayerAim);
    }

    @JsonbTransient
    public Optional<PlayerCollectingAction> getLastAppliedPlayerCollection() {
        return Optional.ofNullable(lastAppliedPlayerCollection);
    }

    @JsonbTransient
    public Optional<PlayerShootingAction> getLastAppliesPlayerShot() {
        return Optional.ofNullable(lastAppliedPlayerShot);
    }
}
