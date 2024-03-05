package io.rizvan.beans;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SessionStorage {
    private final ConcurrentHashMap<String, GameState> gameSessions = new ConcurrentHashMap<>();

    public void addGame(String sessionId, GameState game) {
        gameSessions.put(sessionId, game);
    }

    public Object getGame(String sessionId) {
        return gameSessions.get(sessionId);
    }

    public void removeGame(String sessionId) {
        gameSessions.remove(sessionId);
    }
}
