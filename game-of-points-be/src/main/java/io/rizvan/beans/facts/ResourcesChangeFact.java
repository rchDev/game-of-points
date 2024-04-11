package io.rizvan.beans.facts;

import io.rizvan.beans.ResourcePoint;

import java.util.List;

public class ResourcesChangeFact implements Fact {
    private final List<ResourcePoint> resources;
    private final boolean success;

    public ResourcesChangeFact(List<ResourcePoint> resources, boolean success) {
        this.resources = resources;
        this.success = success;
    }

    public List<ResourcePoint> getResources() {
        return resources;
    }

    @Override
    public boolean actionSucceeded() {
        return success;
    }
}
