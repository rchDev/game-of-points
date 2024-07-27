package io.rizvan.beans.knowledge;

import io.rizvan.beans.ResourcePoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResourcePointKnowledge extends KnowledgeItem<List<ResourcePoint>> {
    private List<ResourcePoint> resources;

    public ResourcePointKnowledge() {
        this.resources = new CopyOnWriteArrayList<>();
    }

    public ResourcePointKnowledge(List<ResourcePoint> resources) {
        this.resources = resources;
    }

    @Override
    public List<ResourcePoint> getValue() {
        return resources;
    }

    @Override
    public void setValue(List<ResourcePoint> value) {
        this.resources = value;
    }
}
