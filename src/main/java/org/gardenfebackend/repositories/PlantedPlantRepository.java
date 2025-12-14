package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Garden;
import org.gardenfebackend.models.PlantedPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlantedPlantRepository extends JpaRepository<PlantedPlant, UUID> {
    List<PlantedPlant> findByGarden(Garden garden);
    Optional<PlantedPlant> findByGardenAndXAndY(Garden garden, Integer x, Integer y);
    void deleteByGarden(Garden garden);
}

