package io.rizvan.beans;

import io.rizvan.beans.actors.agent.actions.ActionType;
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

    private ActionType lastAgentAction = null;

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


    public GameState(GameState other) {
        this.player = new Player(other.player);
        this.agent = new Agent(other.agent);
        this.zone = new Zone(other.zone);
        this.time = other.time;
        this.lastUpdateTime = other.lastUpdateTime;
        this.deltaBetweenUpdates = other.deltaBetweenUpdates;
        this.rng = other.rng;
        // Manual deep copy for List with new ResourcePoint objects
        for (ResourcePoint res : other.resources) {
            this.resources.add(new ResourcePoint(res));
        }
        this.lastAppliedPlayerMovement = other.lastAppliedPlayerMovement != null
                ? new PlayerMovementAction(other.lastAppliedPlayerMovement) : null;
        this.lastAppliedPlayerAim = other.lastAppliedPlayerAim != null
                ? new PlayerAimingAction(other.lastAppliedPlayerAim) : null;
        this.lastAppliedPlayerCollection = other.lastAppliedPlayerCollection != null
                ? new PlayerCollectingAction(other.lastAppliedPlayerCollection) : null;
        this.lastAppliedPlayerShot = other.lastAppliedPlayerShot != null
                ? new PlayerShootingAction(other.lastAppliedPlayerShot) : null;
        this.lastAgentAction = other.lastAgentAction;
        this.factStorage = new FactStorage(other.factStorage);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean hasGameEnded() {
        return time <= 0 || !player.isAlive() || !agent.isAlive();
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
        factStorage.add(new ResourcesChangeFact(resources, true));
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

    public ResourcePoint removeResource(String id) {
        synchronized (resources) {
            Optional<ResourcePoint> removedResource = resources.stream()
                    .filter(resource -> resource.getId().equals(id))
                    .findFirst();

            removedResource.ifPresent(resources::remove);
            factStorage.add(new ResourcesChangeFact(resources, removedResource.isPresent()));
            return removedResource.orElse(null);
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

    /**
     * Funkcija, kuri pritaiko zaidejo atlikta veiksma, zaidejo busenai.
     */
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

    /**
     * Iskviecia agento veiksmo apply metoda, kuris modifikuoja zaidimo busena.
     */
    public void applyAction(AgentAction action) {
        this.lastAgentAction = action.getType();
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

    public void registerFact(Fact fact) {
        factStorage.add(fact);
    }

    private void registerFact(PlayerAction action, boolean success) {
        switch (action.getType()) {
            case "shoot" -> {
                var damage = success ? ((PlayerShootingAction) action).getDamage() : 0;
                factStorage.add(new PlayerShootingFact(damage, success));
            }
            case "aim" -> {
                var aimingAction = (PlayerAimingAction) action;
                var mouseX = aimingAction.getMouseX();
                var mouseY = aimingAction.getMouseY();
                factStorage.add(new PlayerAimFact(mouseX, mouseY, success));
            }
            case "move" -> factStorage.add(new PlayerMovementFact(player.getX(), player.getY(), success));
            case "collect" -> factStorage.add(new PlayerCollectionFact(player.getPoints(), success));
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
        factStorage.add(new GameTimeChangeFact(time, true));
    }

    public ActionType getLastAgentAction() {
        return lastAgentAction;
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
