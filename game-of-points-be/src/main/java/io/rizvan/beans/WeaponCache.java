package io.rizvan.beans;

import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WeaponCache {
    private final ConcurrentHashSet<Weapon> weapons = new ConcurrentHashSet<>();

    public WeaponCache() {
        this.weapons.add(Weapon.Type.SNIPER.get());
        this.weapons.add(Weapon.Type.CARBINE.get());
        this.weapons.add(Weapon.Type.SUB_MACHINE.get());
        this.weapons.add(Weapon.Type.PISTOL.get());
    }

    public List<Weapon> getWeapons() {
        return weapons.stream().toList();
    }
}

