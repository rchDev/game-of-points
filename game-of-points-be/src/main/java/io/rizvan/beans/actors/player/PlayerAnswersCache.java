package io.rizvan.beans.actors.player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerAnswersCache {
    private final ConcurrentHashMap<String, PlayerAnswers> playerAnswersMap = new ConcurrentHashMap<>();

    public PlayerAnswersCache() {
    }

    public PlayerAnswers getPlayerAnswers(String dfSessionId) {
        return playerAnswersMap.get(dfSessionId);
    }

    public void addPlayerAnswers(String dfSessionId, PlayerAnswers playerAnswers) {
        playerAnswersMap.put(dfSessionId, playerAnswers);
    }

    public void addIfAbsent(String dfSessionId, PlayerAnswers playerAnswers) {
        playerAnswersMap.putIfAbsent(dfSessionId, playerAnswers);
    }
}

