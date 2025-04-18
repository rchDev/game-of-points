package io.rizvan.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.rizvan.entities.WeaponEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeaponRepository implements PanacheRepository<WeaponEntity> {
}
