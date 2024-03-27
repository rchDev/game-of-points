package io.rizvan.beans.knowledge;

public class PlayerAmmoCapacityKnowledge extends KnowledgeItem<Integer> {
    private Integer ammoCapacity;

    public PlayerAmmoCapacityKnowledge() {
        this.ammoCapacity = 0;
    }

    public PlayerAmmoCapacityKnowledge(Integer ammoCapacity) {
        this.ammoCapacity = ammoCapacity;
    }

    @Override
    public Integer getValue() {
        return ammoCapacity;
    }

    @Override
    public void setValue(Integer value) {
        ammoCapacity = value;
    }
}
