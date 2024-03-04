package io.rizvan.beans;

import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeaponCache {
    private ConcurrentHashSet<RangedWeapon> weapons = new ConcurrentHashSet<>();

    public WeaponCache() {
        this.weapons.add(new RangedWeapon(0, "Sniper", 3, 0.85f, 2, 40));
        this.weapons.add(new RangedWeapon(1, "Carbine", 1, 0.95f, 5, 30));
        this.weapons.add(new RangedWeapon(2, "Sub-machine", 1, 1.0f, 10, 10));
        this.weapons.add(new RangedWeapon(3, "Pistol", 1, 1.25f, 5, 20));
    }

    public List<RangedWeapon> getWeapons() {
        return weapons.stream().toList();
    }
}

