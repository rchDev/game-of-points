package io.rizvan.beans;

import io.rizvan.beans.facts.Fact;
import io.rizvan.beans.facts.PlayerMovesFact;
import io.rizvan.beans.facts.PlayerShootsFact;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class FactStorage {
    private ConcurrentLinkedQueue<PlayerMovesFact> movementFacts = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<PlayerShootsFact> shootingFacts = new ConcurrentLinkedQueue<>();

    public ConcurrentLinkedQueue<PlayerMovesFact> getPlayerMoveFacts() {
        return movementFacts;
    }

    public ConcurrentLinkedQueue<PlayerShootsFact> getPlayerShootFacts() {
        return shootingFacts;
    }

    public void add(Fact fact) {
        switch (fact.getType()) {
            case "shoot":
                shootingFacts.add((PlayerShootsFact) fact);
                break;
            case "move":
                movementFacts.add((PlayerMovesFact) fact);
                break;
            default:
                throw new IllegalArgumentException("Unknown Fact type: " + fact.getType());
        }
    }

    public ConcurrentLinkedQueue<PlayerMovesFact> getMovementFacts() {
        return movementFacts;
    }

    public ConcurrentLinkedQueue<PlayerShootsFact> getShootingFacts() {
        return shootingFacts;
    }

    public void clear() {
        movementFacts.clear();
        shootingFacts.clear();
    }
}
