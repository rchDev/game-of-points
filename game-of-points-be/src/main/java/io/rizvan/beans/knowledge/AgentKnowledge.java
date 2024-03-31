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
    private PlayerAmmoCapacityKnowledge playerAmmoCapacity;
    private PlayerSpeedKnowledge playerSpeed;
    private PlayerDamageKnowledge playerDamage;
    private PlayerHPKnowledge playerHitPoints;
    private PlayerPointsKnowledge playerPoints;
    private ResourcePointKnowledge resourcePoints;
    private GameTimeKnowledge timeLeft;
    private PlayerReachKnowledge playerReach;
    private WeaponCache possibleWeapons;

    public static double REACH_DISTANCE_OFFSET = 50.0;

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
        this.playerAmmoCapacity = new PlayerAmmoCapacityKnowledge();
        this.playerReach = new PlayerReachKnowledge();

        this.shotCount.setKnown(true);
    }

    public PlayerPositionKnowledge getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Double x, Double y, boolean isKnown) {
        playerPosition.setValue(new Coord<>(x, y));
        playerPosition.setKnown(isKnown);
    }

    public PlayerAimKnowledge getPlayerAim() {
        return playerAim;
    }

    public void setPlayerAim(Double x, Double y, boolean isKnown) {
        playerAim.setValue(new Coord<>(x, y));
        playerAim.setKnown(isKnown);
    }

    public ShotCountKnowledge getShotCount() {
        return shotCount;
    }

    public void setShotCount(Integer shotCount, boolean isKnown) {
        this.shotCount.setValue(shotCount);
        this.shotCount.setKnown(isKnown);
    }

    public PlayerAmmoCapacityKnowledge getPlayerAmmoCapacity() {
        return playerAmmoCapacity;
    }

    public void setPlayerAmmoCapacity(Integer capacity, boolean isKnown) {
        playerAmmoCapacity.setValue(capacity);
        playerAmmoCapacity.setKnown(isKnown);
    }

    public PlayerSpeedKnowledge getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(Double speed, boolean isKnown) {
        this.playerSpeed.setValue(speed);
        this.playerSpeed.setKnown(isKnown);
    }

    public PlayerDamageKnowledge getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(Integer damage, boolean isKnown) {
        this.playerDamage.setValue(damage);
        this.playerSpeed.setKnown(isKnown);
    }

    public PlayerHPKnowledge getPlayerHitPoints() {
        return playerHitPoints;
    }

    public void setPlayerHitPoints(Integer playerHP, boolean isKnown) {
        this.playerHitPoints.setValue(playerHP);
        this.playerHitPoints.setKnown(isKnown);
    }

    public PlayerPointsKnowledge getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(Integer playerPoints, boolean isKnown) {
        this.playerPoints.setValue(playerPoints);
        this.playerPoints.setKnown(isKnown);
    }

    public ResourcePointKnowledge getResourcePoints() {
        return resourcePoints;
    }

    public void setResourcePoints(List<ResourcePoint> resourcePoints, boolean isKnown) {
        this.resourcePoints.setValue(resourcePoints);
        this.resourcePoints.setKnown(isKnown);
    }

    public GameTimeKnowledge getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft, boolean isKnown) {
        this.timeLeft.setValue(timeLeft);
        this.timeLeft.setKnown(isKnown);
    }

    public PlayerReachKnowledge getPlayerReach() {
        return playerReach;
    }

    public void setPlayerReach(Double playerReach, boolean isKnown) {
        this.playerReach.setValue(playerReach);
        this.playerReach.setKnown(isKnown);
    }

    public boolean isPlayerClose(Double agentX, Double agentY) {
        Double playerX = playerPosition.getValue().getX();
        Double playerY = playerPosition.getValue().getY();

        var agentDistanceToPlayer = Math.sqrt(Math.pow(agentX - playerX, 2) + Math.pow(agentY - playerY, 2));
        var confrontationalDistance = playerReach.getValue() + AgentKnowledge.REACH_DISTANCE_OFFSET;

        return agentDistanceToPlayer <= confrontationalDistance;
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
