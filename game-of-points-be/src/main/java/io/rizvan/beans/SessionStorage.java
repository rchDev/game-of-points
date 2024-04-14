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
    private final ConcurrentHashMap<String, ConcurrentHashMap<Long, GameState>> gameStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GameState> latestGameStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void addGameState(String sessionId, GameState state) {
        if (!gameStates.containsKey(sessionId)) {
            gameStates.put(sessionId, new ConcurrentHashMap<>());
        }
        gameStates.get(sessionId).put(state.getLastUpdateTime(), state);
    }

    public void removeGameStates(String sessionId) {
        gameStates.remove(sessionId);
    }

    public GameState getLatestGameState(String sessionId) {
        if (latestGameStates.containsKey(sessionId)) {
            return null;
        }
        return latestGameStates.get(sessionId);
    }
    public void removeGameState(String sessionId, long timeStamp) {
        if (gameStates.containsKey(sessionId)) {
            gameStates.get(sessionId).remove(timeStamp);
        }
    }

    public GameState getGameState(String sessionId, long timeStamp) {
        return gameStates.get(sessionId).get(timeStamp);
    }

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public CopyOnWriteArrayList<GameState> getGameStates(String sessionId) {
        if (!gameStates.containsKey(sessionId)) {
            return new CopyOnWriteArrayList<>();
        }
        return new CopyOnWriteArrayList<>(gameStates.get(sessionId).values());
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

    public void setLatestGameState(String sessionId, GameState gameState) {
        latestGameStates.put(sessionId, gameState);
    }

    public void removeLatestGameState(String sessionId) {
        latestGameStates.remove(sessionId);
    }
}
