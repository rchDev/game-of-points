package io.rizvan;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.FactStorage;
import io.rizvan.beans.GameState;
import io.rizvan.beans.SessionStorage;
import io.rizvan.utils.RandomNumberGenerator;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class GameStateUpdateScheduler {

    @Inject
    RandomNumberGenerator rng;
    @Inject
    FactStorage factStorage;
    @Inject
    SessionStorage sessionStorage;
    @Inject
    EventBus eventBus;
    @Inject
    Vertx vertx;

//    void onServerStart(@Observes StartupEvent ev) {
//        updateGameState();
//    }

    @ConsumeEvent("game.created")
    public void updateGameState(String sessionId) {
        if (sessionStorage.getSessionIds().isEmpty()) return;

        // ALL GAME LOGIC GOES HERE

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

        var x = rng.getInteger(10, 1300);
        var y = rng.getInteger(10, 800);

        var gameState = sessionStorage.getGameState(sessionId);
        gameState.addResource(x, y);
        scheduleNextRPCreation(sessionId);
    }

    @ConsumeEvent("game.created")
    public void scheduleRPCreation(String sessionId) {
        int time = rng.getInteger(2, 5);
        vertx.setTimer(time * 1000L, id -> createNewResourcePoint(sessionId));
    }

    private void scheduleNextRPCreation(String sessionId) {
        int time = rng.getInteger(2, 5);
        vertx.setTimer(time * 1000L, id -> createNewResourcePoint(sessionId));
    }
}