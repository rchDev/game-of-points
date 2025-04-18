package io.rizvan;

import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.GameState;
import io.rizvan.beans.SessionStorage;
import io.rizvan.beans.playerActions.PlayerAction;
import io.rizvan.utils.RandomNumberGenerator;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
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

    @PostConstruct
    public void init() {
        if (sessionStorage.getSessionIds().isEmpty()) return;
        for (var id : sessionStorage.getSessionIds()) {
            var gameState = sessionStorage.getLatestGameState(id);

            if (gameState == null) continue;

            if (gameState.hasGameEnded()) {
                eventBus.publish("game.closed", id);
                continue;
            }

            gameState.setLastUpdateTime(Instant.now().toEpochMilli());
        }
    }

    @ConsumeEvent("game.created")
    public void updateGameState(String sessionId) {
        if (sessionStorage.getSessionIds().isEmpty()) return;

        for (var id : sessionStorage.getSessionIds()) {
            var gameState = sessionStorage.getLatestGameState(id);
            if (gameState == null) continue;

            if (gameState.hasGameEnded()) {
                eventBus.publish("game.closed", id);
                continue;
            }

            var clonedGameState = new GameState(gameState);
            var now = Instant.now().toEpochMilli();
            clonedGameState.setDeltaBetweenUpdates(now - clonedGameState.getLastUpdateTime());
            clonedGameState.setLastUpdateTime(now);

            var playerActions = sessionStorage.getPlayerActions(id);
            if (playerActions == null) continue;

            List<PlayerAction> actionsToProcess = new ArrayList<>();
            playerActions.drainTo(actionsToProcess, MAX_ACTIONS_PER_TICK);

            for (var playerAction : actionsToProcess) {
                clonedGameState.applyAction(playerAction);
            }
            var agent = clonedGameState.getAgent();
            agent.reason(clonedGameState);

            sessionStorage.addGameState(id, clonedGameState);
        }

        for (var id : sessionStorage.getSessionIds()) {
            eventBus.publish("game.update", id);
        }

        scheduleNextGameStateUpdate(sessionId);
    }

    private void scheduleNextGameStateUpdate(String sessionId) {
        vertx.setTimer(50, id -> updateGameState(sessionId));
    }

    /**
     * Funkcija, isukanti Resource Point sukurima. Funkcija yra kvieciama, kuomet ivyksta game.created ivykis.
     */
    @ConsumeEvent("game.created")
    public void scheduleRPCreation(String sessionId) {
        int time = rng.getInteger(2, 5);
        vertx.setTimer(time * 1000L, id -> createNewResourcePoint(sessionId));
    }

    /**
     * Funkcija, tam tikrai sesijai sukurianti Resource Point ir isukanti naujo Resource Point sukurima ateityje.
     */
    private void createNewResourcePoint(String sessionId) {
        if (sessionStorage.getSession(sessionId) == null) return;

        var gameState = sessionStorage.getLatestGameState(sessionId);

        var x = rng.getInteger(
                GameState.RESOURCE_SIZE / 2,
                gameState.getZone().getWidth() - GameState.RESOURCE_SIZE / 2);

        var y = rng.getInteger(
                GameState.RESOURCE_SIZE / 2,
                gameState.getZone().getHeight() - GameState.RESOURCE_SIZE / 2);

        gameState.addResource(x, y);
        scheduleRPCreation(sessionId);
    }

    /**
     * Funkcija, kuri kiekvienai zaidimo sesijai inicializuoja timeri.
     */
    @ConsumeEvent("game.created")
    public void scheduleGameTimer(String sessionId) {
        timerId = vertx.setPeriodic(1000L, id -> {
            var gameState = sessionStorage.getLatestGameState(sessionId);
            if (gameState == null) return;

            var time = gameState.getTime();

            if (time <= 0) {
                vertx.cancelTimer(timerId);
                return;
            }
            gameState.setTime(time - 1);
        });
    }
}