package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public abstract class PlayerAction implements Comparable<PlayerAction> {
    protected String type;
    protected long clientTimestamp = 0;
    protected long serverTimestamp = 0;

    public PlayerAction() {}

    public PlayerAction(PlayerAction other) {
        this.type = other.type;
        this.clientTimestamp = other.clientTimestamp;
        this.serverTimestamp = other.serverTimestamp;
    }

    public abstract boolean apply(GameState gameState);

    public abstract boolean isLegal(GameState gameState);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getClientTimestamp() {
        return clientTimestamp;
    }

    public void setClientTimestamp(long clientTimestamp) {
        this.clientTimestamp = clientTimestamp;
    }

    public long getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    @Override
    public int compareTo(PlayerAction other) {
        return Long.compare(serverTimestamp, other.getServerTimestamp());
    }
}
