package io.rizvan.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mood_weapon_associations")
public class PlayerMoodWeaponEntity extends PanacheEntity {
    @ManyToOne
    @JoinColumn(name = "mood_id", nullable = false)
    private PlayerMoodEntity mood;

    @ManyToOne
    @JoinColumn(name = "weapon_id", nullable = false)
    private WeaponEntity weapon;

    public PlayerMoodWeaponEntity() {}

    public PlayerMoodWeaponEntity(PlayerMoodEntity mood, WeaponEntity weapon) {
        this.mood = mood;
        this.weapon = weapon;
    }

    public PlayerMoodEntity getMood() {
        return mood;
    }

    public void setMood(PlayerMoodEntity mood) {
        this.mood = mood;
    }

    public WeaponEntity getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponEntity weapon) {
        this.weapon = weapon;
    }

}