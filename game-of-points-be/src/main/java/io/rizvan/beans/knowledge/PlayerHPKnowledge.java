package io.rizvan.beans.knowledge;

public class PlayerHPKnowledge extends KnowledgeItem<Integer> {
    private Integer hitPoints;

    public PlayerHPKnowledge() {
        this.hitPoints = 3;
        setName("hit_points");
    }

    public PlayerHPKnowledge(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public Integer getValue() {
        return hitPoints;
    }

    @Override
    public void setValue(Integer value) {
        this.hitPoints = value;
    }
}
