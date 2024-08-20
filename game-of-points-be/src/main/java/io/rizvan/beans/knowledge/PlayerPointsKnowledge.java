package io.rizvan.beans.knowledge;

public class PlayerPointsKnowledge extends KnowledgeItem<Integer> {
    private Integer totalPoints;

    public PlayerPointsKnowledge() {
        this.totalPoints = 0;
        setName("points");
    }

    public PlayerPointsKnowledge(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public Integer getValue() {
        return totalPoints;
    }

    @Override
    public void setValue(Integer value) {
        this.totalPoints = value;
    }
}
