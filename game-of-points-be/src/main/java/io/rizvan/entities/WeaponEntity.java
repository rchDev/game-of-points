package io.rizvan.entities;

import io.rizvan.beans.Weapon;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "weapons")
public class WeaponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int damage;
    private double speedModifier;
    private int ammoCapacity;
    private double range;
    private long rechargeTimeMs;

    @ManyToMany
    @JoinTable(
            name = "weapon_player_moods",
            joinColumns = @JoinColumn(name = "weapon_id"),
            inverseJoinColumns = @JoinColumn(name = "player_mood_id")
    )
    private Set<PlayerMoodEntity> moods = new HashSet<>();

    // Constructors, getters, and setters
    public WeaponEntity() {}

    public WeaponEntity(Weapon weapon) {
        this.name = weapon.getName();
        this.damage = weapon.getDamage();
        this.speedModifier = weapon.getSpeedModifier();
        this.ammoCapacity = weapon.getAmmoCapacity();
        this.range = weapon.getRange();
        this.rechargeTimeMs = weapon.getRechargeTimeMilli();
    }

    public WeaponEntity(String name, int damage, double speedModifier, int ammoCapacity, double range, long rechargeTimeMs) {
        this.name = name;
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.ammoCapacity = ammoCapacity;
        this.range = range;
        this.rechargeTimeMs = rechargeTimeMs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getAmmoCapacity() {
        return ammoCapacity;
    }

    public void setAmmoCapacity(int ammoCapacity) {
        this.ammoCapacity = ammoCapacity;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public long getRechargeTimeMs() {
        return rechargeTimeMs;
    }

    public void setRechargeTimeMs(long rechargeTimeMs) {
        this.rechargeTimeMs = rechargeTimeMs;
    }

    public Set<PlayerMoodEntity> getMoods() {
        return moods;
    }

    public void setMoods(Set<PlayerMoodEntity> moods) {
        this.moods = moods;
    }
}
