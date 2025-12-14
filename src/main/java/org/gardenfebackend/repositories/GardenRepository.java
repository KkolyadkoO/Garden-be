package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Garden;
import org.gardenfebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GardenRepository extends JpaRepository<Garden, UUID> {
    List<Garden> findByUser(User user);
    Optional<Garden> findByIdAndUser(UUID id, User user);
}

