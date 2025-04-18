package io.rizvan.beans.knowledge;

import io.rizvan.beans.actors.player.PlayerAnswers;

public class PlayerAnswersKnowledge extends KnowledgeItem<PlayerAnswers> {
    private PlayerAnswers answers;

    @Override
    public PlayerAnswers getValue() {
        return answers;
    }

    @Override
    public void setValue(PlayerAnswers answers) {
        this.answers = answers;
    }
}
