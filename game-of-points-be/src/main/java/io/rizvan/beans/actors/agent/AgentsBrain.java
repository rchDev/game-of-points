package io.rizvan.beans.actors.agent;
import io.rizvan.beans.GameState;
import io.rizvan.beans.knowledge.AgentKnowledge;


public interface AgentsBrain {
    void reason(GameState gameState);

    AgentKnowledge getKnowledge();
}
