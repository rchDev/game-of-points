package io.rizvan.beans.actors;
import io.rizvan.beans.ResourcePoint;

import java.util.List;

public interface AgentsBrain {
    void senseShot(int damage);
    void senseTime(int gameTimeLeft);
    void senseMovement(double x, double y);
    void senseResourceCollection(int pointsCollected);
    void senseResourceChange(List<ResourcePoint> resources);
    void reason(Agent agent);
}
