package io.rizvan.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.rizvan.entities.PlayerMoodEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerMoodRepository implements PanacheRepository<PlayerMoodEntity> {
}