package io.rizvan.utils;

import io.rizvan.beans.actors.player.PlayerAnswers;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class PlayerAnswersProducer {
    @Produces
    @ApplicationScoped
    public PlayerAnswers createPlayerAnswers() {
        return new PlayerAnswers();
    }
}
