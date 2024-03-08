package io.rizvan.beans;

import io.rizvan.beans.facts.Fact;
import io.rizvan.beans.facts.PlayerMovesFact;
import io.rizvan.beans.facts.PlayerShootsFact;
import io.rizvan.beans.facts.ResourceCollectionFact;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class FactStorage {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<PlayerMovesFact>> movementFacts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<PlayerShootsFact>> shootingFacts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ResourceCollectionFact>> collectionFacts = new ConcurrentHashMap<>();

    public ConcurrentLinkedQueue<PlayerMovesFact> getPlayerMovementFacts(String sessionId) {
        return movementFacts.get(sessionId);
    }

    public ConcurrentLinkedQueue<PlayerShootsFact> getPlayerShootingFacts(String sessionId) {
        return shootingFacts.get(sessionId);
    }

    public ConcurrentLinkedQueue<ResourceCollectionFact> getResourceCollectionFacts(String sessionId) {
        return collectionFacts.get(sessionId);
    }

    public void add(String sessionId, Fact fact) {
        switch (fact.getType()) {
            case "shoot":
                if (!shootingFacts.containsKey(sessionId)) {
                    shootingFacts.put(sessionId, new ConcurrentLinkedQueue<>());
                }
                shootingFacts.get(sessionId).add((PlayerShootsFact) fact);
                break;
            case "move":
                if (!movementFacts.containsKey(sessionId)) {
                    movementFacts.put(sessionId, new ConcurrentLinkedQueue<>());
                }
                movementFacts.get(sessionId).add((PlayerMovesFact) fact);
                break;
            case "collection":
                if (!collectionFacts.containsKey(sessionId)) {
                    collectionFacts.put(sessionId, new ConcurrentLinkedQueue<>());
                }
                collectionFacts.get(sessionId).add((ResourceCollectionFact) fact);
                break;
            default:
                throw new IllegalArgumentException("Unknown Fact type: " + fact.getType());
        }
    }
}
