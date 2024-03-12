package io.rizvan.beans;

import io.rizvan.beans.playerActions.PlayerAction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

@ApplicationScoped
public class SessionStorage {
    @Inject PlayerActionQueue actionQueue;
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

    public CopyOnWriteArrayList<String> getSessionIds() {
        return new CopyOnWriteArrayList<>(sessions.keySet());
    }

    public PriorityBlockingQueue<PlayerAction> getPlayerActions(String sessionId) {
        return actionQueue.getAllForSession(sessionId);
    }

    public void addPlayerAction(String sessionId, PlayerAction action) {
        actionQueue.add(sessionId, action);
    }
}
