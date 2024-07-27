package io.rizvan.beans.facts;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class FactStorage {
    private PlayerMovementFact latestMovementFact;
    private GameTimeChangeFact latestGameTimeChangeFact;
    private PlayerAimFact latestPlayerAimFact;
    private PlayerCollectionFact latestPlayerCollectionFact;
    private ResourcesChangeFact latestResourcesChangeFact;
    private List<Fact> otherFacts = new CopyOnWriteArrayList<>();

    public FactStorage() {}

    public FactStorage(FactStorage other) {
        this.latestMovementFact = other.latestMovementFact;
        this.latestGameTimeChangeFact = other.latestGameTimeChangeFact;
        this.latestPlayerAimFact = other.latestPlayerAimFact;
        this.latestPlayerCollectionFact = other.latestPlayerCollectionFact;
        this.latestResourcesChangeFact = other.latestResourcesChangeFact;
        this.otherFacts = new CopyOnWriteArrayList<>(other.otherFacts);
    }

    public void add(Fact fact) {
        if (fact instanceof PlayerMovementFact) {
            latestMovementFact = (PlayerMovementFact) fact;
        } else if (fact instanceof GameTimeChangeFact) {
            latestGameTimeChangeFact = (GameTimeChangeFact) fact;
        } else if (fact instanceof PlayerAimFact) {
            latestPlayerAimFact = (PlayerAimFact) fact;
        } else if (fact instanceof PlayerCollectionFact) {
            latestPlayerCollectionFact = (PlayerCollectionFact) fact;
        } else if (fact instanceof ResourcesChangeFact) {
            latestResourcesChangeFact = (ResourcesChangeFact) fact;
        } else {
            otherFacts.add(fact);
        }
    }

    public CopyOnWriteArrayList<Fact> getAll() {
        CopyOnWriteArrayList<Fact> combinedFacts = new CopyOnWriteArrayList<>();

        if (latestMovementFact != null) {
            combinedFacts.add(latestMovementFact);
        }
        if (latestGameTimeChangeFact != null) {
            combinedFacts.add(latestGameTimeChangeFact);
        }
        if (latestPlayerAimFact != null) {
            combinedFacts.add(latestPlayerAimFact);
        }
        if (latestPlayerCollectionFact != null) {
            combinedFacts.add(latestPlayerCollectionFact);
        }
        if (latestResourcesChangeFact != null) {
            combinedFacts.add(latestResourcesChangeFact);
        }

        if (otherFacts != null) {
            combinedFacts.addAllAbsent(otherFacts);
        }

        return combinedFacts;
    }

    public void clear() {
        latestMovementFact = null;
        latestGameTimeChangeFact = null;
        latestPlayerAimFact = null;
        latestPlayerCollectionFact = null;
        latestResourcesChangeFact = null;
        otherFacts.clear();
    }
}
