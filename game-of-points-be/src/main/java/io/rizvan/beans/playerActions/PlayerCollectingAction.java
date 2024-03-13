package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.ResourcePoint;

public class PlayerCollectingAction extends PlayerAction {
    ResourcePoint resource;

    public ResourcePoint getResource() {
        return resource;
    }

    public void setResource(ResourcePoint resource) {
        this.resource = resource;
    }

    @Override
    public boolean apply(GameState gameState) {
        return false;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        return false;
    }
}
