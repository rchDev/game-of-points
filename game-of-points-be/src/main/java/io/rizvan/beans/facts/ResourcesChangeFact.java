package io.rizvan.beans.facts;

import io.rizvan.beans.ResourcePoint;

import java.util.List;

public class ResourcesChangeFact implements Fact {
    private final List<ResourcePoint> resources;

    public ResourcesChangeFact(List<ResourcePoint> resources) {
        this.resources = resources;
    }

    public List<ResourcePoint> getResources() {
        return resources;
    }
}
