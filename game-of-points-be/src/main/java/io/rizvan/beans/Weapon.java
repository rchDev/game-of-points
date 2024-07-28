package io.rizvan.beans;

import java.time.Instant;

public class Weapon {
    public enum Type {

        BUBBLE_BLASTER(0, "Bubble Blaster", 2, 0.90f, 5, 10, 1500),
        FROSTY_FLUTE(1, "Frosty Flute", 1, 0.95f, 8, 12, 2000),
        SPARKLE_STAFF(2, "Sparkle Staff", 3, 1.0f, 4, 8, 1800),
        WHIMSY_WAND(3, "Whimsy Wand", 4, 1.10f, 3, 15, 3000),
        JELLYBEAN_JAVELIN(4, "Jellybean Javelin", 3, 0.85f, 6, 7, 1300),
        CANDY_CANE_CLUB(5, "Candy Cane Club", 2, 0.95f, 5, 6, 1000),
        POPCORN_PISTOL(6, "Popcorn Pistol", 2, 1.00f, 7, 14, 2500),
        LOLLIPOP_LANCE(7, "Lollipop Lance", 4, 1.20f, 4, 9, 2200),
        MARSHMALLOW_MACE(8, "Marshmallow Mace", 3, 0.90f, 6, 11, 1700),
        TOFFEE_THROWER(9, "Toffee Thrower", 1, 0.85f, 8, 5, 900),
        GUMMY_GUN(10, "Gummy Gun", 2, 1.0f, 5, 7, 2000),
        FIZZY_FINGER(11, "Fizzy Finger", 4, 1.20f, 3, 12, 2400),
        COOKIE_CATAPULT(12, "Cookie Catapult", 3, 0.90f, 6, 8, 1600),
        SUGARCANE_SPEAR(13, "Sugarcane Spear", 1, 0.95f, 7, 3, 500),
        TAFFY_TWISTER(14, "Taffy Twister", 2, 0.90f, 6, 9, 1400),
        CARAMEL_CROSSBOW(15, "Caramel Crossbow", 4, 1.20f, 3, 7, 1900),
        PEPPERMINT_PIKE(16, "Peppermint Pike", 3, 0.85f, 6, 11, 2100),
        JELLYBEAN_JAVELIN_2(17, "Jellybean Javelin", 3, 0.90f, 6, 6, 1100),
        BUBBLEGUM_BLADE(18, "Bubblegum Blade", 2, 1.0f, 5, 8, 2000),
        HONEYCOMB_HAMMER(19, "Honeycomb Hammer", 1, 0.95f, 7, 5, 1000);

        private final Weapon weapon;
        Type(int id, String name, int damage, double speedModifier, int ammo, double range, int rechargeTimeMs) {
            this.weapon = new Weapon(id, name, damage, speedModifier, ammo, range, rechargeTimeMs);
        }

        public Weapon get() {
            return weapon;
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

    public Weapon(Weapon original) {
        this.id = original.id;
        this.name = original.name;
        this.damage = original.damage;
        this.speedModifier = original.speedModifier;
        this.ammo = original.ammo;
        this.ammoCapacity = original.ammoCapacity;
        this.range = original.range;
        this.shotTime = original.shotTime;
        this.rechargeTimeMs = original.rechargeTimeMs;
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
        if (ammo <= 0 || isRecharging()) {
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

    public boolean isRecharging() {
        return (Instant.now().toEpochMilli() - shotTime) < rechargeTimeMs;
    }

    public long getRechargeTimeLeft() {
        long timeSinceLastShot = Instant.now().toEpochMilli() - shotTime;
        return Math.max(rechargeTimeMs - (timeSinceLastShot), 0);
    }
}
