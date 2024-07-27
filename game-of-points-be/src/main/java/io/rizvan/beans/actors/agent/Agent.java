package io.rizvan.beans.actors.agent;

import io.rizvan.beans.GameState;
import io.rizvan.beans.actors.CompetingEntity;
import io.rizvan.beans.Weapon;
import io.rizvan.beans.knowledge.AgentKnowledge;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Agent extends CompetingEntity {

    public AgentsBrain brain;

    private AgentChoice currentChoice;

    public void setCurrentChoice(AgentChoice choice) {
        this.currentChoice = choice;
    }

    public AgentChoice getCurrentChoice() {
        return currentChoice;
    }

    public enum Type {
        SNIPER(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.SNIPER.get()),
        SOLDIER(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.CARBINE.get()),
        SCOUT(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.SUB_MACHINE.get()),
        SPEED_DEMON(3, 0, 0, 50, 50, 1.0, 0, Weapon.Type.PISTOL.get());

        private final Agent agent;

        Type(int hp, double x, double y, int width, int height, double speed, int points, Weapon weapon) {
            this.agent = new Agent(hp, x, y, width, height, speed, points, weapon);
        }

        public Agent get() {
            return agent;
        }
    }

    public Agent(int hitPoints, double x, double y, int width, int height, double speed, int points, Weapon weapon) {
        super(hitPoints, x, y, width, height, speed, points, weapon);
        this.brain = new DroolsBrain();
    }

    public Agent(Agent other) {
        super(other);
        this.brain = other.brain;
        this.currentChoice = other.currentChoice;
    }

    public static Agent getRandom() {
        List<Agent> agents = Arrays.asList(Type.SNIPER.get(), Type.SOLDIER.get(), Type.SCOUT.get(), Type.SPEED_DEMON.get());
        int index = new Random().nextInt(agents.size() - 1);

        return agents.get(index);
    }

    public void reason(GameState gameState) {
        brain.reason(gameState);
    }

    public AgentKnowledge getKnowledge() {
        return brain.getKnowledge();
    }
}
