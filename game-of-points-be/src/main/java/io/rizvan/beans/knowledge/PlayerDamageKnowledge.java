package io.rizvan.beans.knowledge;

import io.rizvan.beans.Weapon;

public class PlayerDamageKnowledge extends KnowledgeItem<Integer> {
    private Integer damage;

    public PlayerDamageKnowledge() {
        this.damage = 0;
        setName(Weapon.Stat.DAMAGE.getName());
    }

    public PlayerDamageKnowledge(Integer damage) {
        this.damage = damage;
    }

    @Override
    public Integer getValue() {
        return damage;
    }

    @Override
    public void setValue(Integer value) {
        this.damage = value;
    }
}
