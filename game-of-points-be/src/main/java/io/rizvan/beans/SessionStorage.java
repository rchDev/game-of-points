package io.rizvan.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class SessionStorage {
    private final ConcurrentHashMap<String, GameState> gameStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void addGameState(String sessionId, GameState state) {
        gameStates.put(sessionId, state);
    }

    public void removeGameState(String sessionId) {
        gameStates.remove(sessionId);
    }

    public GameState getGameState(String sessionId) {
        return gameStates.get(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public CopyOnWriteArrayList<GameState> getGameStates() {
        return new CopyOnWriteArrayList<>(gameStates.values());
    }

    public CopyOnWriteArrayList<Session> getSessions() {
        return new CopyOnWriteArrayList<>(sessions.values());
    }
}
