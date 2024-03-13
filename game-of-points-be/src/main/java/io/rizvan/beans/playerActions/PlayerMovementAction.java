package io.rizvan.beans.playerActions;

import io.rizvan.beans.GameState;

public class PlayerMovementAction extends PlayerAction {
    private double dx;
    private double dy;

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
        player.setX(player.getX() + dx);
        player.setY(player.getY() + dy);

        return true;
    }

    @Override
    public boolean isLegal(GameState gameState) {
        var clientTimeDiff = clientTimestamp - gameState.getLastAppliedClientTimestamp();
        var serverTimeDiff = serverTimestamp - gameState.getLastAppliedServerTimestamp();
        var distancePerMilli = gameState.getPlayer().getSpeed();

        var maxLegalDistanceClient = distancePerMilli * clientTimeDiff;
        var maxLegalDistanceServer = distancePerMilli * serverTimeDiff;

        var distanceTravelled = Math.sqrt(dx * dx + dy * dy);

        if (clientTimeDiff <= serverTimeDiff + 20 || clientTimeDiff >= serverTimeDiff + 20) {
            return distanceTravelled <= maxLegalDistanceClient;
        }

        return distanceTravelled <= maxLegalDistanceServer;
    }
}
