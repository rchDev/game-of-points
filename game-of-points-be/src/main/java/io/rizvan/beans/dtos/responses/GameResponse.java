package io.rizvan.beans.dtos.responses;

import io.rizvan.beans.GameState;

public class GameResponse {
    private GameState gameState;
    private String sessionId;
    private Long lastProcessedTimestamp;

    public GameResponse() {
    }

    public GameResponse(GameState gameState, String sessionId) {
        this.gameState = gameState;
        this.sessionId = sessionId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getLastProcessedTimestamp() {
        return lastProcessedTimestamp;
    }

    public void setLastProcessedTimestamp(Long lastProcessedTimestamp) {
        this.lastProcessedTimestamp = lastProcessedTimestamp;
    }
}
