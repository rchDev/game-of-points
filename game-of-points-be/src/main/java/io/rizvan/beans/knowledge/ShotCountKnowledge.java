package io.rizvan.beans.knowledge;

public class ShotCountKnowledge extends KnowledgeItem<Integer> {
    private Integer shotCount;

    public ShotCountKnowledge() {
        this.shotCount = 0;
    }

    public ShotCountKnowledge(Integer shotCount) {
        this.shotCount = shotCount;
    }

    @Override
    public Integer getValue() {
        return shotCount;
    }

    @Override
    public void setValue(Integer value) {
        this.shotCount = value;
    }
}
