package io.rizvan.beans.actors;
import io.rizvan.beans.facts.Fact;
import io.rizvan.beans.ResourcePoint;

import java.util.List;

public interface AgentsBrain {
    void reason(List<Fact> facts, Agent agent);
}
