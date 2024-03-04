package io.rizvan.beans;

public class RangedWeapon {
    private int id;
    private String name;
    private int damage;
    private float speedModifier;
    private int ammo;
    private float range;

    public RangedWeapon(int id, String name, int damage, float speedModifier, int ammo, float range) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.ammo = ammo;
        this.range = range;
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

    public float getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
