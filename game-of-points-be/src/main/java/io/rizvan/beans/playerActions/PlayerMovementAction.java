package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerMovementAction extends PlayerAction {
    private double dx = 0;
    private double dy = 0;

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public boolean apply(GameState gameState) {
        var player = gameState.getPlayer();

        var updatedX = player.getX() + dx;
        var updatedY = player.getY() + dy;

        var topBound = player.getHitBox().getHeight() / 2.0;
        var leftBound = player.getHitBox().getWidth() / 2.0;

        var bottomBound = gameState.getZone().getHeight() - player.getHitBox().getHeight() / 2.0;
        var rightBound = gameState.getZone().getWidth() - player.getHitBox().getWidth() / 2.0;

        if (updatedX < leftBound && dx < 0) {
            updatedX = leftBound;
        } else if (updatedX > rightBound && dx > 0) {
            updatedX = rightBound;
        }

        if (updatedY < topBound && dy < 0) {
            updatedY = topBound;
        } else if (updatedY > bottomBound && dy > 0) {
            updatedY = bottomBound;
        }

        player.setX(updatedX);
        player.setY(updatedY);

        return true;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        var lastMovement = gameState.getLastAppliedPlayerMovement()
                .orElseGet(PlayerMovementAction::NullAction);

        var clientTimeDiff = clientTimestamp - lastMovement.clientTimestamp;
        var serverTimeDiff = serverTimestamp - lastMovement.serverTimestamp;

        var distancePerMilli = gameState.getPlayer().getSpeed();

        var maxLegalDistanceClient = distancePerMilli * clientTimeDiff;
        var maxLegalDistanceServer = distancePerMilli * serverTimeDiff;

        var distanceTravelled = Math.sqrt(dx * dx + dy * dy);
//        return true;

        if (clientTimeDiff <= serverTimeDiff + 20 || clientTimeDiff >= serverTimeDiff + 20) {
            return distanceTravelled <= maxLegalDistanceClient;
        }

        return distanceTravelled <= maxLegalDistanceServer;
    }

    public static PlayerMovementAction NullAction() {
        return new PlayerMovementAction();
    }
}
