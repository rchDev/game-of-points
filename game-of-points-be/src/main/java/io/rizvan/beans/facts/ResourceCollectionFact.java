package io.rizvan.beans.facts;

import io.rizvan.beans.actors.CompetingEntity;

public class ResourceCollectionFact extends Fact {
    @Override
    public boolean check(CompetingEntity entity1, CompetingEntity entity2) {
        return false;
    }
}
