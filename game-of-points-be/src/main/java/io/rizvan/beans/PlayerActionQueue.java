package io.rizvan.beans;

import io.rizvan.beans.playerActions.PlayerAction;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@ApplicationScoped
public class PlayerActionQueue {
    private final ConcurrentHashMap<String, PriorityBlockingQueue<PlayerAction>> playerActions;

    public PlayerActionQueue() {
        playerActions = new ConcurrentHashMap<>();
    }

    public void add(String sessionId, PlayerAction playerAction) {
        if (!playerActions.containsKey(sessionId)) {
            playerActions.put(sessionId, new PriorityBlockingQueue<>());
        }
        playerActions.get(sessionId).add(playerAction);
    }

    @Nullable
    public PriorityBlockingQueue<PlayerAction> getAllForSession(String sessionId) {
        return playerActions.get(sessionId);
    }

    public ConcurrentHashMap<String, PriorityBlockingQueue<PlayerAction>> getAll() {
        return playerActions;
    }
}
