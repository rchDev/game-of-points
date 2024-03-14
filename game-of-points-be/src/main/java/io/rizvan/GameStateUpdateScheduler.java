package io.rizvan;

import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.PlayerActionQueue;
import io.rizvan.beans.GameState;
import io.rizvan.beans.SessionStorage;
import io.rizvan.beans.playerActions.PlayerAction;
import io.rizvan.utils.RandomNumberGenerator;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GameStateUpdateScheduler {
    @Inject
    RandomNumberGenerator rng;
    @Inject
    SessionStorage sessionStorage;
    @Inject
    EventBus eventBus;
    @Inject
    Vertx vertx;

    private long timerId = -1;

    public static final int MAX_ACTIONS_PER_TICK = 1000;

    @ConsumeEvent("game.created")
    public void updateGameState(String sessionId) {
        if (sessionStorage.getSessionIds().isEmpty()) return;

        for (var id : sessionStorage.getSessionIds()) {
            var gameState = sessionStorage.getGameState(id);

            if (gameState == null) continue;

            var playerActions = sessionStorage.getPlayerActions(id);
            if (playerActions == null) continue;

            List<PlayerAction> actionsToProcess = new ArrayList<>();
            playerActions.drainTo(actionsToProcess, MAX_ACTIONS_PER_TICK);

            for (var playerAction : actionsToProcess) {
                gameState.applyAction(playerAction);
            }
        }

        for (var id : sessionStorage.getSessionIds()) {
            eventBus.publish("game.update", id);
        }

        scheduleNextGameStateUpdate(sessionId);
    }

    private void scheduleNextGameStateUpdate(String sessionId) {
        vertx.setTimer(100, id -> updateGameState(sessionId));
    }

    private void createNewResourcePoint(String sessionId) {
        if (sessionStorage.getSession(sessionId) == null) return;

        var gameState = sessionStorage.getGameState(sessionId);

        var x = rng.getInteger(
                GameState.RESOURCE_SIZE / 2,
                gameState.getZone().getWidth() - GameState.RESOURCE_SIZE / 2);

        var y = rng.getInteger(
                GameState.RESOURCE_SIZE / 2,
                gameState.getZone().getHeight() - GameState.RESOURCE_SIZE / 2);

        gameState.addResource(x, y);
        scheduleRPCreation(sessionId);
    }

    @ConsumeEvent("game.created")
    public void scheduleRPCreation(String sessionId) {
        int time = rng.getInteger(2, 5);
        vertx.setTimer(time * 1000L, id -> createNewResourcePoint(sessionId));
    }

    @ConsumeEvent("game.created")
    public void scheduleGameTimer(String sessionId) {

        timerId = vertx.setPeriodic(1000L, id -> {
            var gameState = sessionStorage.getGameState(sessionId);
            var time = gameState.getTime();

            if (time <= 0) {
                vertx.cancelTimer(timerId);
                return;
            }
            gameState.setTime(time - 1);
        });
    }
}