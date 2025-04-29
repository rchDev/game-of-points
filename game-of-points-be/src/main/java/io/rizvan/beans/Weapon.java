package io.rizvan.beans;

import java.time.Instant;
import java.util.HashMap;


public class Weapon {
    public class WeaponStat<T> {
        private final T value;

        public WeaponStat(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
    public enum Type {

        BUBBLE_BLASTER(0, "Bubble Blaster", 1, 1.00f, 4, 24, 1500),
        FROSTY_FLUTE(1, "Frosty Flute", 1, 1.05f, 4, 20, 1000),
        SPARKLE_STAFF(2, "Sparkle Staff", 2, 0.85f, 3, 20, 1500),
        WHIMSY_WAND(3, "Whimsy Wand", 3, 0.80f, 2, 8, 2000),
        JELLYBEAN_JAVELIN(4, "Jellybean Javelin", 2, 0.80f, 3, 16, 2000),
        CANDY_CANE_CLUB(5, "Candy Cane Club", 1, 1.05f, 4, 20, 1000),
        POPCORN_PISTOL(6, "Popcorn Pistol", 1, 1.00f, 4, 16, 1500),
        LOLLIPOP_LANCE(7, "Lollipop Lance", 3, 0.70f, 2, 8, 2500),
        MARSHMALLOW_MACE(8, "Marshmallow Mace", 2, 0.85f, 3, 12, 2000),
        TOFFEE_THROWER(9, "Toffee Thrower", 1, 1.10f, 5, 16, 1000),
        GUMMY_GUN(10, "Gummy Gun", 1, 1.00f, 5, 20, 1000),
        FIZZY_FINGER(11, "Fizzy Finger", 3, 0.80f, 2, 12, 2000),
        COOKIE_CATAPULT(12, "Cookie Catapult", 2, 0.90f, 3, 16, 2000),
        SUGARCANE_SPEAR(13, "Sugarcane Spear", 1, 1.10f, 4, 12, 1500),
        TAFFY_TWISTER(14, "Taffy Twister", 1, 1.05f, 5, 20, 1000),
        CARAMEL_CROSSBOW(15, "Caramel Crossbow", 3, 0.70f, 2, 8, 2000),
        PEPPERMINT_PIKE(16, "Peppermint Pike", 2, 0.85f, 3, 16, 1500),
        JELLYBEAN_JAVELIN_2(17, "Jellybean Javelin", 2, 0.80f, 3, 16, 2000),
        BUBBLEGUM_BLADE(18, "Bubblegum Blade", 1, 1.00f, 3, 20, 1500),
        HONEYCOMB_HAMMER(19, "Honeycomb Hammer", 2, 0.85f, 3, 12, 2000),
        TWIZZLE_TWIRLER(20, "Twizzle Twirler", 1, 0.70f, 4, 20, 1000),
        CUPCAKE_CRUSHER(21, "Cupcake Crusher", 1, 0.80f, 4, 20, 1000),
        GUMDROP_GAUNTLET(22, "Gumdrop Gauntlet", 1, 0.85f, 4, 20, 1000),
        TOOTHSOME_TORCH(23, "Toothsome Torch", 1, 0.90f, 4, 20, 1000),
        LICORICE_LASHER(24, "Licorice Lasher", 1, 0.90f, 4, 20, 1000),
        FUDGE_FLOGGER(25, "Fudge Flogger", 3, 0.85f, 2, 20, 2000),
        TART_TWISTER(26, "Tart Twister", 3, 0.90f, 2, 15, 2000),
        MOUSSE_MAULER(27, "Mousse Mauler", 3, 1.00f, 2, 15, 2000),
        TRUFFLE_THRASHER(28, "Truffle Thrasher", 3, 1.05f, 2, 15, 2000),
        CANDY_CLAWS(29, "Candy Claws", 3, 1.10f, 2, 15, 2000),
        MARZIPAN_MALLET(30, "Marzipan Mallet", 2, 0.70f, 3, 20, 2000),
        JELLY_JABBER(31, "Jelly Jabber", 2, 0.80f, 3, 20, 2000),
        CARAMEL_CRUSH(32, "Caramel Crush", 2, 1.00f, 3, 15, 2000),
        NOUGAT_NUDGE(33, "Nougat Nudge", 2, 1.05f, 3, 15, 2000),
        SUGAR_SWATTER(34, "Sugar Swatter", 2, 1.10f, 3, 15, 2000);

        private final Weapon weapon;
        Type(int id, String name, int damage, double speedModifier, int ammo, double range, int rechargeTimeMs) {
            this.weapon = new Weapon(id, name, damage, speedModifier, ammo, range, rechargeTimeMs);
        }

        public Weapon get() {
            return weapon;
        }
    }

    public enum Stat {
        DAMAGE("damage", Integer.class),
        SPEED_MOD("speed_mod", Double.class),
        USES("uses", Integer.class),
        RANGE("range", Double.class),
        RECHARGE_TIME("recharge_time", Long.class);

        private final Class<? extends Number> type;
        private final String name;

        Stat(String name, Class<? extends Number> type) {
            this.name = name;
            this.type = type;
        }

        public Class<? extends Number> getType() {
            return type;
        }

        public <T extends Number> T cast(Number value) {
            return (T) value;
        }

        public String getName() {
            return name;
        }

        public static Stat fromName(String name) {
            for (Stat stat : Stat.values()) {
                if (stat.getName().equalsIgnoreCase(name)) {
                    return stat;
                }
            }
            throw new IllegalArgumentException("No enum constant for name: " + name);
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
    private HashMap<Stat, Number> stats;

    public Weapon(int id, String name, int damage, double speedModifier, int ammo, double range, long rechargeTimeMs) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.speedModifier = speedModifier;
        this.ammo = ammo;
        this.ammoCapacity = ammo;
        this.range = scaleRangeStat(range, 1);
        this.rechargeTimeMs = rechargeTimeMs;

        this.stats = new HashMap<>();
        stats.put(Stat.DAMAGE, damage);
        stats.put(Stat.SPEED_MOD, speedModifier);
        stats.put(Stat.USES, ammo);
        stats.put(Stat.RANGE, range);
        stats.put(Stat.RECHARGE_TIME, rechargeTimeMs);
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
        this.stats = original.stats;
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

    public Number getStat(Stat stat) {
        return stats.getOrDefault(stat, 0);
    }

    public <T extends Number> T getStatAs(Stat stat, Class<T> type) {
        var statValue = stats.get(stat);
        if (type.isInstance(statValue)) {
            return (T) statValue;
        } else {
            throw new ClassCastException("Cannot cast value to " + type.getName());
        }
    }

    private double scaleRangeStat(double value, int scaleFactor) {
        return value * Math.pow(10.0, scaleFactor);
    }
}
