package io.rizvan.beans;

import io.rizvan.beans.playerActions.PlayerAction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.Session;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

@ApplicationScoped
public class SessionStorage {
    @Inject PlayerActionQueue actionQueue;
    private final ConcurrentHashMap<String, ConcurrentHashMap<Long, GameState>> gameStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<GameState>> gameStateLists = new ConcurrentHashMap<>();


    public static final int GAME_STATE_HISTORY_SIZE = 1000;
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

        if (!gameStateLists.containsKey(sessionId)) {
            gameStateLists.put(sessionId, new CopyOnWriteArrayList<>());
        }

        var gameStateList = gameStateLists.get(sessionId);
        if (gameStateList.size() > GAME_STATE_HISTORY_SIZE) {
            var removedGameState = gameStateList.remove(0);
            gameStates.get(sessionId).remove(removedGameState.getLastUpdateTime());
        }

        gameStates.get(sessionId).put(state.getLastUpdateTime(), state);
        gameStateList.add(state);
    }

    public void removeGameStates(String sessionId) {
        gameStates.remove(sessionId);
        gameStateLists.remove(sessionId);
    }

    public GameState getLatestGameState(String sessionId) {
        if (!gameStateLists.containsKey(sessionId)) {
            return null;
        }
        var gameStateHistory  = gameStateLists.get(sessionId);

        return gameStateHistory.isEmpty() ?
                null :
                gameStateHistory.get(gameStateHistory.size() - 1);
    }

    public GameState getGameState(String sessionId, long lastUpdateTime) {
        if (!gameStates.containsKey(sessionId)) {
            return null;
        }

        return gameStates.get(sessionId).get(lastUpdateTime);
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
}
