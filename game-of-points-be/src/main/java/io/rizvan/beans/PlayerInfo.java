package io.rizvan.beans;

import jakarta.inject.Inject;

import java.util.List;

public class PlayerInfo {

    @Inject
    WeaponCache weaponCache;

    private int hp;
    private double x;
    private double y;
    private double speed;
    private int ammo;
    private double reach;
    private List<RangedWeapon> weapons;

    public PlayerInfo(int hp, double x, double y, double speed, int ammo, double reach) {
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.ammo = ammo;
        this.reach = reach;
        this.weapons = weaponCache.getWeapons();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }
}
