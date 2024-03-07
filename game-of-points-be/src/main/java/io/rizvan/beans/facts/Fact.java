package io.rizvan.beans.facts;

import io.rizvan.beans.actors.CompetingEntity;

public abstract class Fact {
    protected String type;
    protected long timestamp;

    public abstract boolean check(CompetingEntity entity1, CompetingEntity entity2);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
