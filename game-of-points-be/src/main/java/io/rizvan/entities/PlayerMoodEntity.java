package io.rizvan.entities;

import io.rizvan.beans.actors.player.PlayerMood;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "player_moods")
public class PlayerMoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlayerMood mood;

    @ManyToMany(mappedBy = "moods")
    private Set<WeaponEntity> weapons = new HashSet<>();

    // Constructors, getters, and setters
    public PlayerMoodEntity() {}

    public PlayerMoodEntity(PlayerMood mood) {
        this.mood = mood;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerMood getMood() {
        return mood;
    }

    public void setMood(PlayerMood mood) {
        this.mood = mood;
    }

    public Set<WeaponEntity> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<WeaponEntity> weapons) {
        this.weapons = weapons;
    }
}