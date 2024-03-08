package io.rizvan;

import io.quarkus.runtime.StartupEvent;
import io.rizvan.beans.FactStorage;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class GameStateUpdateScheduler {

    @Inject
    FactStorage factStorage;
    @Inject
    EventBus eventBus;

    @Inject
    Vertx vertx;

    void onServerStart(@Observes StartupEvent ev) {
        scheduleNextTask();
    }

    private void updateGameState() {
        eventBus.publish("game.update", "game state updated");
        scheduleNextTask();
    }

    private void scheduleNextTask() {
        vertx.setTimer(100, id -> updateGameState());
    }
}