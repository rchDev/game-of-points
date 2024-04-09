package io.rizvan.beans.actors.agent.actions;

import io.rizvan.beans.GameState;
import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.actors.agent.Agent;

import java.util.List;

public class AgentAvoidsAction extends AgentAction {
    private static final double DEG_TO_RAD = Math.PI / 180.0;

    @Override
    public void apply(GameState gameState) {
        int movementAngle = chooseBestMovementAngle(gameState);
        gameState.applyAction(new AgentMovesAction(movementAngle));
    }

    private int chooseBestMovementAngle(GameState gameState) {
        var gameZone = gameState.getZone();
        var agent = gameState.getAgent();
        var player = gameState.getPlayer();

        int bestAngle = 1;
        double bestAngleQuality = Double.NEGATIVE_INFINITY;

        for (var angle = 1; angle <= 360; angle++) {
            var line = getLine(
                    agent.getX(),
                    agent.getY(),
                    angle,
                    gameZone.getWidth(),
                    gameZone.getHeight()
            );

            var playerToLineEndDist = calculateDistance(player.getX(), player.getY(), line.getEndX(), line.getEndY());

            var distToPlayerReachRadius = playerToLineEndDist - player.getReach();
            var resourcesFound = countResourcesWithinBeam(agent, angle, 45, gameState.getResources());
            var angleQuality = calculateStateQuality(distToPlayerReachRadius, resourcesFound);

            if (angleQuality > bestAngleQuality) {
                bestAngleQuality = angleQuality;
                bestAngle = angle;
            }
        }

        return bestAngle;
    }

    private static double calculateStateQuality(double distToPlayerReachRadius, int resourcesFound) {
        if (distToPlayerReachRadius <= 0) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return distToPlayerReachRadius + (distToPlayerReachRadius * 10 * resourcesFound);
        }
    }

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static int countResourcesWithinBeam(Agent agent, double centralLineAngle, double searchBeamAngle, List<ResourcePoint> resources) {
        double lowerBoundAngle = centralLineAngle - searchBeamAngle;
        double upperBoundAngle = centralLineAngle + searchBeamAngle;

        int count = 0;
        for (var resourcePoint : resources) {
            double angleToResource = calculateAngleToPoint(
                    agent.getX(),
                    agent.getY(),
                    resourcePoint.getX(),
                    resourcePoint.getY()
            );

            // Normalize angles to the range [0, 360)
            lowerBoundAngle = Math.floorMod((int) lowerBoundAngle, 360);
            upperBoundAngle = Math.floorMod((int) upperBoundAngle, 360);
            angleToResource = Math.floorMod((int) angleToResource, 360);

            // Check if angle is within the beam's angular boundaries
            if (isAngleWithinBounds(angleToResource, lowerBoundAngle, upperBoundAngle)) {
                count++;
            }
        }
        return count;
    }

    private static double calculateAngleToPoint(double fromX, double fromY, double toX, double toY) {
        // Calculate angle from one point to another
        double angle = Math.toDegrees(Math.atan2(toY - fromY, toX - fromX));
        return angle < 0 ? angle + 360 : angle;
    }

    private static boolean isAngleWithinBounds(double angle, double lowerBound, double upperBound) {
        if (lowerBound < upperBound) {
            return angle >= lowerBound && angle <= upperBound;
        } else { // Case where the beam crosses the 0-degree line
            return angle >= lowerBound || angle <= upperBound;
        }
    }

    private Line getLine(double x, double y, double angle, int width, int height) {
        double angleRadians = angle * DEG_TO_RAD;

        // Direction vector
        double dirX = Math.cos(angleRadians);
        double dirY = Math.sin(angleRadians);

        double intersectionX = (dirX > 0) ? width : 0;
        double intersectionY = (dirY > 0) ? height : 0;

        // Intersection formulas
        double tX = (intersectionX - x) / dirX;
        double tY = (intersectionY - y) / dirY;

        // Calculate the actual intersection point
        double finalX = x + dirX * tX;
        double finalY = y + dirY * tY;

        finalX = Math.min(Math.max(0, finalX), width);
        finalY = Math.min(Math.max(0, finalY), height);

        // Return the length of the line
        return new Line(x, y, finalX, finalY);
    }

    private static class Line {
        private double startX;
        private double startY;
        private double endX;
        private double endY;

        public Line(double startX, double startY, double endX, double endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public double getStartX() {
            return startX;
        }

        public void setStartX(double startX) {
            this.startX = startX;
        }

        public double getStartY() {
            return startY;
        }

        public void setStartY(double startY) {
            this.startY = startY;
        }

        public double getEndX() {
            return endX;
        }

        public void setEndX(double endX) {
            this.endX = endX;
        }

        public double getEndY() {
            return endY;
        }

        public void setEndY(double endY) {
            this.endY = endY;
        }
    }
}
