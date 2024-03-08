package io.rizvan;

import io.quarkus.runtime.StartupEvent;
import io.rizvan.beans.FactStorage;
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
    EventBus eventBus;
    @Inject
    Vertx vertx;

    void onServerStart(@Observes StartupEvent ev) {
        updateGameState();
        scheduleNextRPCreation();
    }

    private void updateGameState() {
        eventBus.publish("game.update", "game state updated");
        scheduleNextGameStateUpdate();
    }

    private void scheduleNextGameStateUpdate() {
        vertx.setTimer(100, id -> updateGameState());
    }

    private void createNewResourcePoint(long timeTaken) {
        System.out.println("!!!NEW RESOURCE POINT ADDED!!! after time:" + timeTaken);
        scheduleNextRPCreation();
    }

    private void scheduleNextRPCreation() {
        int time = rng.getIntInRangeIncludes(2, 5);
        vertx.setTimer(time * 1000L, id -> createNewResourcePoint(time));
    }
}