package io.rizvan.beans;

import io.rizvan.beans.facts.Fact;
import io.rizvan.beans.facts.PlayerMovesFact;
import io.rizvan.beans.facts.PlayerShootsFact;
import io.rizvan.beans.facts.ResourceCollectionFact;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class FactStorage {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<PlayerMovesFact>> movementFacts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, CopyOnWriteArrayList<PlayerShootsFact>> shootingFacts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, CopyOnWriteArrayList<ResourceCollectionFact>> collectionFacts = new ConcurrentHashMap<>();

    public CopyOnWriteArrayList<PlayerMovesFact> getPlayerMovementFacts(String sessionId) {
        return movementFacts.get(sessionId);
    }

    public CopyOnWriteArrayList<PlayerShootsFact> getPlayerShootingFacts(String sessionId) {
        return shootingFacts.get(sessionId);
    }

    public CopyOnWriteArrayList<ResourceCollectionFact> getResourceCollectionFacts(String sessionId) {
        return collectionFacts.get(sessionId);
    }

    public void add(String sessionId, Fact fact) {
        switch (fact.getType()) {
            case "shoot":
                if (!shootingFacts.containsKey(sessionId)) {
                    shootingFacts.put(sessionId, new CopyOnWriteArrayList<>());
                }
                shootingFacts.get(sessionId).add((PlayerShootsFact) fact);
                break;
            case "move":
                if (!movementFacts.containsKey(sessionId)) {
                    movementFacts.put(sessionId, new CopyOnWriteArrayList<>());
                }
                movementFacts.get(sessionId).add((PlayerMovesFact) fact);
                break;
            case "collection":
                if (!collectionFacts.containsKey(sessionId)) {
                    collectionFacts.put(sessionId, new CopyOnWriteArrayList<>());
                }
                collectionFacts.get(sessionId).add((ResourceCollectionFact) fact);
                break;
            default:
                throw new IllegalArgumentException("Unknown Fact type: " + fact.getType());
        }
    }

    public void clearPlayerMovementFacts(String sessionId) {
        var facts = movementFacts.get(sessionId);

        if (facts == null) return;

        facts.clear();
    }

    public void clearCollectionFacts(String sessionId) {
        var facts = collectionFacts.get(sessionId);

        if (facts == null) return;

        facts.clear();
    }
}
