package io.rizvan.beans.dtos.responses;

import io.rizvan.beans.actors.Player;
import io.rizvan.beans.actors.agent.Agent;

public class GameEndedResponse {
    private final Agent agent;
    private final Player player;
    private final Integer timeLeft;
    private final Boolean gameHasEnded;

    public GameEndedResponse(Agent agent, Player player, Integer timeLeft, Boolean gameHasEnded) {
        this.agent = agent;
        this.player = player;
        this.timeLeft = timeLeft;
        this.gameHasEnded = gameHasEnded;
    }

    public Agent getAgent() {
        return agent;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public Boolean getGameHasEnded() {
        return gameHasEnded;
    }
}
