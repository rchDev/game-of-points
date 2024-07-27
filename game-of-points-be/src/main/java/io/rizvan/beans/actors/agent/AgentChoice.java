package io.rizvan.beans.actors.agent;

import io.rizvan.beans.actors.agent.choices.ChoiceType;

public interface AgentChoice extends Cloneable {
    ChoiceType getType();
}
