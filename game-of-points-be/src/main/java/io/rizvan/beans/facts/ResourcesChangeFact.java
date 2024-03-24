package io.rizvan.beans.facts;

import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.actors.AgentsBrain;

import java.util.List;

public class ResourcesChangeFact implements Fact {
    private final List<ResourcePoint> resources;

    public ResourcesChangeFact(List<ResourcePoint> resources) {
        this.resources = resources;
    }

    @Override
    public void supplyInfo(AgentsBrain brain) {

    }

    public List<ResourcePoint> getResources() {
        return resources;
    }
}
