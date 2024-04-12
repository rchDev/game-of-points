package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.actors.GameEntity;
import io.rizvan.beans.actors.agent.Agent;

import java.time.Instant;

public class AgentMovesAction implements AgentAction {
    private final Double destinationX;
    private final Double destinationY;
    private final Integer angle;
    private final ActionType actionType = ActionType.MOVE;

    public AgentMovesAction(GameEntity destination) {
        this.destinationX = destination.getX();
        this.destinationY = destination.getY();
        this.angle = null;
    }

    public AgentMovesAction(double x, double y) {
        this.destinationX = x;
        this.destinationY = y;
        this.angle = null;
    }

    public AgentMovesAction(int angle) {
        this.destinationX = null;
        this.destinationY = null;
        this.angle = angle;
    }

    @Override
    public void apply(GameState gameState) {
        if (destinationX != null && destinationY != null && angle == null) {
            moveToAnEntity(gameState, destinationX, destinationY);
        } else {
            moveAtAnAngle(gameState, angle);
        }
    }

    @Override
    public ActionType getType() {
        return actionType;
    }

    public void moveAtAnAngle(GameState gameState, int angleDegrees) {
        var agent = gameState.getAgent();

        // Convert angle to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate direction vector based on the angle
        double directionX = Math.cos(angleRadians);
        double directionY = Math.sin(angleRadians);

        var delta = gameState.getDeltaBetweenUpdates();
        var deltaBetweenUpdateAndNow = Instant.now().toEpochMilli() - gameState.getLastUpdateTime();
        var combinedDelta = delta + deltaBetweenUpdateAndNow;

        // Calculate the move distance
        double moveDistance = agent.getSpeed() * combinedDelta;

        move(gameState, directionX, directionY, moveDistance);
    }

    public void moveToAnEntity(GameState gameState, double destinationX, double destinationY) {
        var agentX = gameState.getAgent().getX();
        var agentY = gameState.getAgent().getY();

        double directionX = destinationX - agentX;
        double directionY = destinationY - agentY;

        // Normalize the direction vector to get the unit vector
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        if (magnitude != 0) {
            directionX /= magnitude;
            directionY /= magnitude;
        }

        var delta = gameState.getDeltaBetweenUpdates();
        var deltaBetweenUpdateAndNow = Instant.now().toEpochMilli() - gameState.getLastUpdateTime();
        var combinedDelta = delta + deltaBetweenUpdateAndNow;

        double moveDistance = gameState.getAgent().getSpeed() * combinedDelta;
        if (moveDistance > magnitude) {
            moveDistance = magnitude;
        }

        move(gameState, directionX, directionY, moveDistance);
    }

    private void move(GameState gameState, double directionX, double directionY, double moveDistance) {
        var agent = gameState.getAgent();

        double moveX = directionX * moveDistance;
        double moveY = directionY * moveDistance;

        double agentNewX = Math.max(0, Math.min(gameState.getZone().getWidth(), agent.getX() + moveX));
        double agentNewY = Math.max(0, Math.min(gameState.getZone().getHeight(), agent.getY() + moveY));

        agent.setX(agentNewX);
        agent.setY(agentNewY);
    }
}
