package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;

public interface AgentAction {
    void apply(GameState gameState);
    ActionType getType();
}
