package io.rizvan.beans.knowledge;

import io.rizvan.beans.GameState;
import io.rizvan.beans.RangedWeapon;
import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.WeaponCache;
import io.rizvan.beans.actors.Player;
import jakarta.inject.Inject;

import java.util.List;

public class AgentKnowledge {

    private PlayerPositionKnowledge playerPosition;
    private PlayerAimKnowledge playerAim;
    private ShotCountKnowledge shotCount;
    private PlayerSpeedKnowledge playerSpeed;
    private PlayerDamageKnowledge playerDamage;
    private PlayerHPKnowledge playerHitPoints;
    private PlayerPointsKnowledge playerPoints;
    private ResourcePointKnowledge resourcePoints;
    private GameTimeKnowledge timeLeft;
    public WeaponCache possibleWeapons;

    public AgentKnowledge() {
        this.playerPosition = new PlayerPositionKnowledge();
        this.playerAim = new PlayerAimKnowledge();
        this.shotCount = new ShotCountKnowledge();
        this.playerSpeed = new PlayerSpeedKnowledge();
        this.playerDamage = new PlayerDamageKnowledge();
        this.playerHitPoints = new PlayerHPKnowledge();
        this.playerPoints = new PlayerPointsKnowledge();
        this.timeLeft = new GameTimeKnowledge();
        this.resourcePoints = new ResourcePointKnowledge();
        this.possibleWeapons = new WeaponCache();
    }

    public PlayerPositionKnowledge getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(PlayerPositionKnowledge playerPosition) {
        this.playerPosition = playerPosition;
    }

    public PlayerAimKnowledge getPlayerAim() {
        return playerAim;
    }

    public void setPlayerAim(PlayerAimKnowledge playerAim) {
        this.playerAim = playerAim;
    }

    public ShotCountKnowledge getShotCount() {
        return shotCount;
    }

    public void setShotCount(ShotCountKnowledge shotCount) {
        this.shotCount = shotCount;
    }

    public PlayerSpeedKnowledge getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(PlayerSpeedKnowledge playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public PlayerDamageKnowledge getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(PlayerDamageKnowledge playerDamage) {
        this.playerDamage = playerDamage;
    }

    public PlayerHPKnowledge getPlayerHitPoints() {
        return playerHitPoints;
    }

    public void setPlayerHitPoints(PlayerHPKnowledge playerHitPoints) {
        this.playerHitPoints = playerHitPoints;
    }

    public PlayerPointsKnowledge getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(PlayerPointsKnowledge playerPoints) {
        this.playerPoints = playerPoints;
    }

    public ResourcePointKnowledge getResourcePoints() {
        return resourcePoints;
    }

    public void setResourcePoints(ResourcePointKnowledge resourcePoints) {
        this.resourcePoints = resourcePoints;
    }

    public GameTimeKnowledge getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(GameTimeKnowledge timeLeft) {
        this.timeLeft = timeLeft;
    }

    public List<RangedWeapon> getPossibleWeapons() {
        return possibleWeapons.getWeapons();
    }

    public void setPossibleWeapons(WeaponCache possibleWeapons) {
        this.possibleWeapons = possibleWeapons;
    }

    @Override
    public String toString() {
        return "AgentKnowledge{\n" +
                "  playerPosition=" + playerPosition + ",\n" +
                "  playerAim=" + playerAim + ",\n" +
                "  shotCount=" + shotCount + ",\n" +
                "  playerSpeed=" + playerSpeed + ",\n" +
                "  playerDamage=" + playerDamage + ",\n" +
                "  playerHitPoints=" + playerHitPoints + ",\n" +
                "  playerPoints=" + playerPoints + ",\n" +
                "  resourcePoints=" + resourcePoints + ",\n" +
                "  timeLeft=" + timeLeft + ",\n" +
                "  possibleWeapons=" + possibleWeapons + "\n" +
                '}';
    }
}
