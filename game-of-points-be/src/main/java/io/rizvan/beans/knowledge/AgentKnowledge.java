package io.rizvan.beans.knowledge;

import io.rizvan.beans.RangedWeapon;
import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.WeaponCache;
import io.rizvan.utils.Coord;

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

    public void setPlayerPosition(Double x, Double y) {
        this.playerPosition.setValue(new Coord<>(x, y));
    }

    public PlayerAimKnowledge getPlayerAim() {
        return playerAim;
    }

    public void setPlayerAim(Double x, Double y) {
        this.playerAim.setValue(new Coord<>(x, y));
    }

    public ShotCountKnowledge getShotCount() {
        return shotCount;
    }

    public void setShotCount(Integer shotCount) {
        this.shotCount.setValue(shotCount);
    }

    public PlayerSpeedKnowledge getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(Double speed) {
        this.playerSpeed.setValue(speed);
    }

    public PlayerDamageKnowledge getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(Integer damage) {
        this.playerDamage.setValue(damage);
    }

    public PlayerHPKnowledge getPlayerHitPoints() {
        return playerHitPoints;
    }

    public void setPlayerHitPoints(Integer playerHP) {
        this.playerHitPoints.setValue(playerHP);
    }

    public PlayerPointsKnowledge getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(Integer playerPoints) {
        this.playerPoints.setValue(playerPoints);
    }

    public ResourcePointKnowledge getResourcePoints() {
        return resourcePoints;
    }

    public void setResourcePoints(List<ResourcePoint> resourcePoints) {
        this.resourcePoints.setValue(resourcePoints);
    }

    public GameTimeKnowledge getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft.setValue(timeLeft);
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
