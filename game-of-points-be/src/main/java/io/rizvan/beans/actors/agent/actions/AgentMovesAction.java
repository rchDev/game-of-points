package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.actors.GameEntity;

import java.time.Instant;

public class AgentMovesAction extends AgentAction {
    private GameEntity destination;

    public AgentMovesAction(GameEntity destination) {
        this.destination = destination;
    }

    @Override
    public void apply(GameState gameState) {
        var destinationX = destination.getX();
        var destinationY = destination.getY();
        var destinationHitBox = destination.getHitBox();

        var agentX = gameState.getAgent().getX();
        var agentY = gameState.getAgent().getY();
        var agentHitBox = gameState.getAgent().getHitBox();

        double directionX = destinationX - agentX;
        double directionY = destinationY - agentY;

        // Normalize the direction vector to get the unit vector
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        if (magnitude != 0) {
            directionX /= magnitude;
            directionY /= magnitude;
        }

        var delta = gameState.getDeltaBetweenUpdates();
        var deltaBetweenUpdateAndNow = Instant.now().toEpochMilli();
        var combinedDelta = delta + deltaBetweenUpdateAndNow;

        double moveDistance = gameState.getAgent().getSpeed() * (combinedDelta / 1000.0);

        double moveX = directionX * moveDistance;
        double moveY = directionY * moveDistance;

        double agentNewX = Math.max(0, Math.min(gameState.getZone().getWidth(), agentX + moveX));
        double agentNewY = Math.max(0, Math.min(gameState.getZone().getHeight(), agentY + moveY));

        gameState.getAgent().setX(agentNewX);
        gameState.getAgent().setY(agentNewY);
    }

    public GameEntity getDestination() {
        return destination;
    }

    public void setDestination(GameEntity destination) {
        this.destination = destination;
    }
}
