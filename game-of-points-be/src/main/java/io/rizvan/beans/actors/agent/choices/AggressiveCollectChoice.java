package io.rizvan.beans.actors.agent.choices;

import io.rizvan.beans.actors.agent.AgentChoice;

public class AggressiveCollectChoice implements AgentChoice {
    @Override
    public ChoiceType getType() {
        return ChoiceType.AGGRESSIVE_COLLECT;
    }



}
