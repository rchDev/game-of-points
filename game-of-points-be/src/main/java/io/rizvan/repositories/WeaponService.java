package io.rizvan.repositories;

import io.rizvan.beans.Weapon;
import io.rizvan.beans.actors.player.PlayerMood;
import io.rizvan.entities.PlayerMoodEntity;
import io.rizvan.entities.WeaponEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class WeaponService {

    @Inject
    WeaponRepository weaponRepository;

    @Inject
    PlayerMoodRepository playerMoodRepository;

    @Transactional
    public void addWeaponWithMood(Weapon weapon, PlayerMood mood) {
        WeaponEntity weaponEntity = new WeaponEntity(weapon);
        PlayerMoodEntity playerMood = playerMoodRepository.find("mood", mood).firstResult();

        if (playerMood == null) {
            playerMood = new PlayerMoodEntity(mood);
            playerMoodRepository.persist(playerMood);
        }

        weaponEntity.getMoods().add(playerMood);
        weaponRepository.persist(weaponEntity);

    }

    public List<WeaponEntity> getAllWeapons() {
        List<WeaponEntity> weapons = weaponRepository.listAll();
        return weapons;
    }
}
