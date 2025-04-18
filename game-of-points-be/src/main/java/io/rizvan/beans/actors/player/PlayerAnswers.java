package io.rizvan.beans.actors.player;

import java.util.Optional;

public class PlayerAnswers {
    private Optional<String> moodDescription;
    private Optional<String> weaponName;
    private Optional<Double> speed;
    private Optional<Integer> damage;
    private Optional<Integer> uses;
    private Optional<Double> range;
    private Optional<Long> rechargeTime;
    private Optional<PlayerMood> mood;

    public PlayerAnswers() {
        moodDescription = Optional.empty();
        weaponName = Optional.empty();
        speed = Optional.empty();
        damage = Optional.empty();
        uses = Optional.empty();
        range = Optional.empty();
        rechargeTime = Optional.empty();
    }

    @Override
    public String toString() {
        return "PlayerAnswers:\n\tmood=" +
                (moodDescription.orElse("[empty]")) + "\n\tweaponName=" +
                weaponName.orElse("[empty]") + "\n\tspeed=" +
                (speed.isEmpty() ? "[empty]" : speed.get()) + "\n\tdamage=" +
                (damage.isEmpty() ? "[empty]" : damage.get()) + "\n\tuses=" +
                (uses.isEmpty() ? "[empty]" : uses.get()) + "\n\trange=" +
                (range.isEmpty() ? "[empty]" : range.get()) + "\n\trechargeTime=" +
                (rechargeTime.isEmpty() ? "[empty]" : rechargeTime.get()) + "\n}";
    }

    public Optional<String> getMoodDescription() {
        return moodDescription;
    }

    public void setMoodDescription(String mood) {
        this.moodDescription = Optional.of(mood);
    }

    public Optional<String> getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = Optional.of(weaponName);
    }

    public Optional<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = Optional.of(speed);
    }

    public Optional<Integer> getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = Optional.of(damage);
    }

    public Optional<Integer> getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = Optional.of(uses);
    }

    public Optional<Double> getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = Optional.of(range);
    }

    public Optional<Long> getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Long rechargeTime) {
        this.rechargeTime = Optional.of(rechargeTime);
    }

    public void setMood(PlayerMood mood) {
        this.mood = Optional.of(mood);
    }

    public Optional<PlayerMood> getMood() {
        return this.mood;
    }
}