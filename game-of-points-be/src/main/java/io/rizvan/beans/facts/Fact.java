package io.rizvan.beans.facts;

import io.rizvan.beans.CompetingEntity;

public interface Fact {
    boolean check(CompetingEntity entity1, CompetingEntity entity2);
}
