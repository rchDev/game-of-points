package io.rizvan.beans.knowledge;


public class AgentPossibilities {
    private boolean canOneShootPlayer;
    private boolean oneShotByPlayer;
    private boolean fasterThanPlayer;
    private boolean slowerThanPlayer;
    private boolean canReachPlayer;
    private boolean reachedByPlayer;

    private boolean canKillPlayer;
    private boolean killedByPlayer;

    private boolean canWinByPointCollection;

    public boolean canOneShootPlayer() {
        return canOneShootPlayer;
    }

    public void setCanOneShootPlayer(boolean canOneShotPlayer) {
        this.canOneShootPlayer = canOneShotPlayer;
    }

    public boolean isOneShotByPlayer() {
        return oneShotByPlayer;
    }

    public void setOneShotByPlayer(boolean oneShotByPlayer) {
        this.oneShotByPlayer = oneShotByPlayer;
    }

    public boolean isFasterThanPlayer() {
        return fasterThanPlayer;
    }

    public void setFasterThanPlayer(boolean fasterThanPlayer) {
        this.fasterThanPlayer = fasterThanPlayer;
    }

    public boolean isSlowerThanPlayer() {
        return slowerThanPlayer;
    }

    public void setSlowerThanPlayer(boolean slowerThanPlayer) {
        this.slowerThanPlayer = slowerThanPlayer;
    }

    public boolean canReachPlayer() {
        return canReachPlayer;
    }

    public void setCanReachPlayer(boolean canReachPlayer) {
        this.canReachPlayer = canReachPlayer;
    }

    public boolean isReachedByPlayer() {
        return reachedByPlayer;
    }

    public void setReachedByPlayer(boolean reachedByPlayer) {
        this.reachedByPlayer = reachedByPlayer;
    }

    public boolean canKillPlayer() {
        return canKillPlayer;
    }

    public void setCanKillPlayer(boolean canKillPlayer) {
        this.canKillPlayer = canKillPlayer;
    }

    public boolean isKilledByPlayer() {
        return killedByPlayer;
    }

    public void setKilledByPlayer(boolean killedByPlayer) {
        this.killedByPlayer = killedByPlayer;
    }

    public boolean canWinByPointCollection() {
        return canWinByPointCollection;
    }

    public void setCanWinByPointCollection(boolean canWinByPointCollection) {
        this.canWinByPointCollection = canWinByPointCollection;
    }
}