package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.ResourcePoint;

import java.util.Objects;

public class PlayerCollectingAction extends PlayerAction {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean apply(GameState gameState) {
        var points = gameState.getResources()
                .stream()
                .filter(rp -> Objects.equals(rp.getId(), id))
                .findFirst()
                .orElse(new ResourcePoint(0, 0, 0, 0, 0))
                .getPoints();

        gameState.removeResource(id);
        gameState.getPlayer().addPoints(points);
        return true;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        var resourcePoint = gameState.getResources()
                .stream()
                .filter(rp -> Objects.equals(rp.getId(), id))
                .findFirst()
                .orElse(null);

        if (resourcePoint == null) {
            return false;
        }

        var player = gameState.getPlayer();

        return player.getX() < resourcePoint.getX() + resourcePoint.getHitbox().getWidth() &&
                player.getX() + player.getHitBox().getWidth() > resourcePoint.getX() &&
                player.getY() < resourcePoint.getY() + resourcePoint.getHitbox().getHeight() &&
                player.getY() + player.getHitBox().getHeight() > resourcePoint.getY();
    }
}
