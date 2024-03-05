package io.rizvan.beans;

import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeaponCache {
    private final ConcurrentHashSet<RangedWeapon> weapons = new ConcurrentHashSet<>();

    public WeaponCache() {
        this.weapons.add(RangedWeapon.Type.SNIPER.get());
        this.weapons.add(RangedWeapon.Type.CARBINE.get());
        this.weapons.add(RangedWeapon.Type.SUB_MACHINE.get());
        this.weapons.add(RangedWeapon.Type.PISTOL.get());
    }

    public List<RangedWeapon> getWeapons() {
        return weapons.stream().toList();
    }
}

