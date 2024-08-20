package io.rizvan.beans.knowledge;

import io.rizvan.beans.GameState;

public class GameTimeKnowledge extends KnowledgeItem<Integer> {
    private Integer timeLeft;


    public GameTimeKnowledge() {
        this.timeLeft = GameState.GAME_TIME;
        setName("game_time");
    }

    public GameTimeKnowledge(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    public Integer getValue() {
        return timeLeft;
    }

    @Override
    public void setValue(Integer value) {
        this.timeLeft = value;
    }
}
