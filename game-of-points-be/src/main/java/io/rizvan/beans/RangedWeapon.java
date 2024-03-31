package io.rizvan.beans;

public class RangedWeapon {
    public enum Type {
        SNIPER(0, "Sniper", 3, 0.85f, 2, 40),
        CARBINE(1, "Carbine", 1, 0.95f, 5, 30),
        SUB_MACHINE(2, "Sub-machine", 1, 1.0f, 10, 10),
        PISTOL(3, "Pistol", 1, 1.25f, 5, 20);

        private final RangedWeapon weapon;
        Type(int id, String name, int damage, double speedModifier, int ammo, double range) {
            this.weapon = new RangedWeapon(id, name, damage, speedModifier, ammo, range);
        }

        public RangedWeapon get() {
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

    public RangedWeapon(int id, String name, int damage, double speedModifier, int ammo, double range) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.ammo = ammo;
        this.ammoCapacity = ammo;
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

    public double getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
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
}
