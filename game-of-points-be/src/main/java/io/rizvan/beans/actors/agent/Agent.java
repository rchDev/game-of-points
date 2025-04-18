package io.rizvan.beans.actors.agent;

import io.rizvan.beans.GameState;
import io.rizvan.beans.actors.CompetingEntity;
import io.rizvan.beans.Weapon;
import io.rizvan.beans.actors.player.PlayerAnswers;
import io.rizvan.beans.knowledge.AgentKnowledge;
import io.rizvan.entities.WeaponEntity;
import io.rizvan.utils.PythonGateway;

import java.util.List;

public class Agent extends CompetingEntity {
    public AgentsBrain brain;

    private AgentChoice currentChoice;

    public static Agent fromWeapon(Weapon weapon, PythonGateway pythonGateway, PlayerAnswers playerAnswer, List<WeaponEntity> weaponMoodOccurrences) {
        return new Agent(3, 0, 0, 50, 50, 1.0, 0, weapon, pythonGateway, playerAnswer, weaponMoodOccurrences);
    }

    public void setCurrentChoice(AgentChoice choice) {
        this.currentChoice = choice;
    }

    public AgentChoice getCurrentChoice() {
        return currentChoice;
    }


    public Agent(int hitPoints, double x, double y, int width, int height, double speed, int points, Weapon weapon, PythonGateway pythonGateway, PlayerAnswers playerAnswer, List<WeaponEntity> weaponMoodOccurrences) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
        this.brain = new DroolsBrain(pythonGateway, playerAnswer, weaponMoodOccurrences);
    }

    public Agent(Agent other) {
        super(other);
        this.brain = other.brain;
        this.currentChoice = other.currentChoice;
    }

    public void reason(GameState gameState) {
        brain.reason(gameState);
    }

    public AgentKnowledge getKnowledge() {
        return brain.getKnowledge();
    }
}
