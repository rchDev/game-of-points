package io.rizvan.beans;

import jakarta.json.bind.annotation.JsonbTransient;

import java.beans.Transient;
import java.time.Instant;

public class Weapon {
    public enum Type {
        SNIPER(0, "Sniper", 3, 0.85f, 2, 400, 3000),
        CARBINE(1, "Carbine", 1, 0.95f, 5, 300, 1500),
        SUB_MACHINE(2, "Sub-machine", 1, 1.0f, 10, 150, 500),
        PISTOL(3, "Pistol", 1, 1.25f, 5, 200, 1000);

        private final Weapon weapon;
        Type(int id, String name, int damage, double speedModifier, int ammo, double range, int rechargeTimeMs) {
            this.weapon = new Weapon(id, name, damage, speedModifier, ammo, range, rechargeTimeMs);
        }

        public Weapon get() {
            return weapon;
        }
    }

    public enum Name {
        SNIPER("Sniper"),
        CARBINE("Carbine"),
        SUB_MACHINE("Sub-machine"),
        PISTOL("Pistol");

        private String name;
        Name(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private int id;
    private String name;
    private int damage;
    private double speedModifier;
    private int ammo;
    private final int ammoCapacity;
    private double range;
    private long shotTime;
    private final long rechargeTimeMs;

    public Weapon(int id, String name, int damage, double speedModifier, int ammo, double range, long rechargeTimeMs) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.ammo = ammo;
        this.ammoCapacity = ammo;
        this.range = range;
        this.rechargeTimeMs = rechargeTimeMs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getAmmo() {
        return ammo;
    }

    public int shoot() {
        System.out.println("SHOOT RAN");
        if (ammo <= 0 && isRecharging()) {
            return 0;
        }

        this.shotTime = Instant.now().toEpochMilli();
        this.ammo -= 1;
        return damage;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public int getAmmoCapacity() {
        return ammoCapacity;
    }

    public long getRechargeTimeMilli() {
        return rechargeTimeMs;
    }

    @JsonbTransient
    public boolean isRecharging() {
        System.out.println("id: " + id + " time passed: " + (Instant.now().toEpochMilli() - shotTime) + ", rechargeTime: " + rechargeTimeMs);
        return (Instant.now().toEpochMilli() - shotTime) < rechargeTimeMs;
    }

    public long getRechargeTimeLeft() {
        long timeSinceLastShot = Instant.now().toEpochMilli() - shotTime;
        return Math.max(rechargeTimeMs - (timeSinceLastShot), 0);
    }
}
