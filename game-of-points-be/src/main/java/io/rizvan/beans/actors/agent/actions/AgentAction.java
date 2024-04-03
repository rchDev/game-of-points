package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;

public abstract class AgentAction {
    public abstract void apply(GameState gameState);
}
