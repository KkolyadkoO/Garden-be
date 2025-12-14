package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlantRepository extends JpaRepository<Plant, UUID> {
    List<Plant> findByIsVerified(boolean isVerified);
}

