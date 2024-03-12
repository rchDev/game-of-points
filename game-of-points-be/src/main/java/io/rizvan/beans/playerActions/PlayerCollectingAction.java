package io.rizvan.beans.playerActions;

import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.actors.CompetingEntity;

public class PlayerCollectingAction extends PlayerAction {
    ResourcePoint resource;

    @Override
    public boolean check(CompetingEntity entity1, CompetingEntity entity2) {
        return false;
    }

    public ResourcePoint getResource() {
        return resource;
    }

    public void setResource(ResourcePoint resource) {
        this.resource = resource;
    }
}
