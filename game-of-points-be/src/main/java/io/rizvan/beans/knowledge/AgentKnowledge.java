package io.rizvan.beans.knowledge;

import io.rizvan.beans.HitBox;
import io.rizvan.beans.Weapon;
import io.rizvan.beans.ResourcePoint;
import io.rizvan.beans.WeaponCache;
import io.rizvan.beans.actors.CompetingEntity;
import io.rizvan.beans.actors.agent.AgentChoice;
import io.rizvan.beans.actors.agent.actions.AgentAction;
import io.rizvan.beans.actors.player.PlayerAnswers;
import io.rizvan.utils.Coord;
import io.rizvan.utils.Pair;
import jakarta.json.bind.annotation.JsonbTransient;

import java.util.ArrayList;
import java.util.List;

public class AgentKnowledge {
    private final PlayerPositionKnowledge playerPosition;
    private final PlayerAimKnowledge playerAim;
    private final ShotCountKnowledge shotCount;
    private final PlayerAmmoCapacityKnowledge playerAmmoCapacity;
    private final PlayerSpeedKnowledge playerSpeed;
    private final PlayerDamageKnowledge playerDamage;
    private final PlayerRechargeTimeKnowledge rechargeTime;
    private final PlayerHPKnowledge playerHitPoints;
    private final PlayerPointsKnowledge playerPoints;
    private final ResourcePointKnowledge resourcePoints;
    private final PlayerAnswersKnowledge playerAnswers;

    private final GameTimeKnowledge timeLeft;
    private final PlayerReachKnowledge playerReach;
    private final List<Pair<Weapon.Stat, Weapon.Stat>> statRelations;
    private final List<KnowledgeItem> playerStats;
    private AgentChoice agentChoice;
    private WeaponCache possibleWeapons;
    private AgentAction currentAction;
    private PlayerHitBoxKnowledge playerHitBox;

    public static double REACH_DISTANCE_OFFSET = 50.0;

    public AgentKnowledge() {
        this.playerStats = new ArrayList<>();
        this.playerPosition = new PlayerPositionKnowledge();
        this.playerAim = new PlayerAimKnowledge();
        this.shotCount = new ShotCountKnowledge();
        this.rechargeTime = new PlayerRechargeTimeKnowledge();
        this.playerSpeed = new PlayerSpeedKnowledge();
        this.playerDamage = new PlayerDamageKnowledge();
        this.playerHitPoints = new PlayerHPKnowledge();
        this.playerPoints = new PlayerPointsKnowledge();
        this.playerAnswers = new PlayerAnswersKnowledge();
        this.timeLeft = new GameTimeKnowledge();
        this.resourcePoints = new ResourcePointKnowledge();
        this.possibleWeapons = new WeaponCache();
        this.playerAmmoCapacity = new PlayerAmmoCapacityKnowledge();
        this.playerReach = new PlayerReachKnowledge();
        this.playerHitBox = new PlayerHitBoxKnowledge();
        this.shotCount.setKnown(true);

        this.statRelations = new ArrayList<>(){};
        statRelations.add(new Pair<>(Weapon.Stat.DAMAGE, Weapon.Stat.SPEED_MOD));
        statRelations.add(new Pair<>(Weapon.Stat.RECHARGE_TIME, Weapon.Stat.DAMAGE));
        statRelations.add(new Pair<>(Weapon.Stat.RANGE, Weapon.Stat.DAMAGE));
        statRelations.add(new Pair<>(Weapon.Stat.USES, Weapon.Stat.RECHARGE_TIME));

        playerStats.add(playerAmmoCapacity);
        playerStats.add(playerSpeed);
        playerStats.add(playerDamage);
        playerStats.add(playerReach);
        playerStats.add(rechargeTime);
    }

    public PlayerPositionKnowledge getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Double x, Double y, boolean isKnown) {
        playerPosition.setValue(new Coord<>(x, y));
        playerPosition.setKnown(isKnown);
    }

    public PlayerHitBoxKnowledge getPlayerHitBox() {
        return playerHitBox;
    }

    public void setPlayerHitBoxKnowledge(HitBox playerHitBox, boolean isKnown) {
        this.playerHitBox = new PlayerHitBoxKnowledge();
        this.playerHitBox.setValue(playerHitBox);
        this.playerHitBox.setKnown(isKnown);
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

    @JsonbTransient
    public void setPlayerDamage(Integer damage, boolean isKnown) {
        this.playerDamage.setValue(damage);
        this.playerDamage.setKnown(isKnown);
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

    public List<Pair<Weapon.Stat, Weapon.Stat>> getStatRelations() {
        return statRelations;
    }

    public boolean isPlayerClose(Double agentX, Double agentY, HitBox hitBox) {
        Double playerX = playerPosition.getValue().getX();
        Double playerY = playerPosition.getValue().getY();

        var unsafeDistance = playerReach.getValue();
        return CompetingEntity.touchesReachCircle(playerX, playerY, unsafeDistance, agentX, agentY, hitBox);
    }

    public List<Weapon> getPossibleWeapons() {
        return possibleWeapons.getWeapons();
    }

    public void setPossibleWeapons(WeaponCache possibleWeapons) {
        this.possibleWeapons = possibleWeapons;
    }

    public void setAgentChoice(AgentChoice choice) {
        this.agentChoice = choice;
    }

    public AgentChoice getAgentChoice() {
        return agentChoice;
    }

    public AgentAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(AgentAction currentAction) {
        this.currentAction = currentAction;
    }

    public PlayerAnswersKnowledge getPlayerAnswers() {
        return playerAnswers;
    }

    public void setPlayerAnswers(PlayerAnswers playerAnswers) {
        this.playerAnswers.setValue(playerAnswers);
        this.playerAnswers.setKnown(true);
    }

    public PlayerRechargeTimeKnowledge getRechargeTime() {
        return rechargeTime;
    }

    public void setPlayerRechargeTime(long rechargeTime, boolean isKnown) {
        this.rechargeTime.setValue(rechargeTime);
        this.rechargeTime.setKnown(isKnown);
    }

    public List<KnowledgeItem> getPlayerStats() {
        return playerStats;
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
