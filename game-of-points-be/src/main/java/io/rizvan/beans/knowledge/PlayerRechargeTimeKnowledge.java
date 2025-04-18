package io.rizvan.beans.knowledge;

import io.rizvan.beans.Weapon;

public class PlayerRechargeTimeKnowledge extends KnowledgeItem<Long> {
    private long rechargeTime;


    public PlayerRechargeTimeKnowledge() {
        setName(Weapon.Stat.RECHARGE_TIME.getName());
    }
    @Override
    public Long getValue() {
        return rechargeTime;
    }

    @Override
    public void setValue(Long value) {
        this.rechargeTime = value;
    }
}
