package io.rizvan.beans;

import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class WeaponCache {
    private final ConcurrentHashSet<Weapon> weapons = new ConcurrentHashSet<>();

    public WeaponCache() {
        this.weapons.addAll(Arrays.stream(Weapon.Type.values()).map(Weapon.Type::get).toList());
    }

    public List<Weapon> getWeapons() {
        return weapons.stream().toList();
    }
}

