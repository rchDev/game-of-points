package io.rizvan.utils;

import io.rizvan.beans.actors.player.PlayerAnswersCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class PlayerAnswersCacheProducer {
    @Produces
    @ApplicationScoped
    public PlayerAnswersCache createPlayerAnswersCache() {
        return new PlayerAnswersCache();
    }
}
