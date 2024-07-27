package io.rizvan.beans.knowledge;

import io.rizvan.beans.HitBox;

public class PlayerHitBoxKnowledge extends KnowledgeItem<HitBox> {
    private HitBox hitBox;

    @Override
    public HitBox getValue() {
        return hitBox;
    }

    @Override
    public void setValue(HitBox value) {
        this.hitBox = value;
    }
}
