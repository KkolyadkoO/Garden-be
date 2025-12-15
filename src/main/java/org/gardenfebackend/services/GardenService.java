package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateGardenRequest;
import org.gardenfebackend.dtos.requests.PlantPlantRequest;
import org.gardenfebackend.dtos.responses.CellInfo;
import org.gardenfebackend.dtos.responses.GardenResponse;
import org.gardenfebackend.enums.GardenType;
import org.gardenfebackend.models.Garden;
import org.gardenfebackend.models.Plant;
import org.gardenfebackend.models.PlantedPlant;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.GardenRepository;
import org.gardenfebackend.repositories.PlantRepository;
import org.gardenfebackend.repositories.PlantedPlantRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GardenService {

    private final GardenRepository gardenRepository;
    private final PlantRepository plantRepository;
    private final PlantedPlantRepository plantedPlantRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new IllegalStateException("Пользователь не аутентифицирован");
    }

    @Transactional
    public GardenResponse create(CreateGardenRequest request) {
        User user = getCurrentUser();

        Garden garden = Garden.builder()
                .type(request.getType())
                .name(request.getName())
                .user(user)
                .build();

        Garden savedGarden = gardenRepository.save(garden);
        return mapToResponse(savedGarden);
    }

    @Transactional(readOnly = true)
    public List<GardenResponse> getAll() {
        User user = getCurrentUser();
        return gardenRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GardenResponse getById(UUID id) {
        User user = getCurrentUser();
        Garden garden = gardenRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Огород с ID " + id + " не найден"));
        return mapToResponse(garden);
    }

    @Transactional
    public void plantPlant(UUID gardenId, PlantPlantRequest request) {
        User user = getCurrentUser();
        Garden garden = gardenRepository.findByIdAndUser(gardenId, user)
                .orElseThrow(() -> new RuntimeException("Огород с ID " + gardenId + " не найден"));

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new RuntimeException("Растение с ID " + request.getPlantId() + " не найдено"));

        validateCoordinates(garden.getType(), request.getX(), request.getY());

        PlantedPlant existingPlant = plantedPlantRepository.findByGardenAndXAndY(garden, request.getX(), request.getY())
                .orElse(null);

        java.time.LocalDate currentDate = java.time.LocalDate.now();
        
        if (existingPlant != null) {
            existingPlant.setPlant(plant);
            existingPlant.setPlantedDate(currentDate);
            plantedPlantRepository.save(existingPlant);
        } else {
            PlantedPlant plantedPlant = PlantedPlant.builder()
                    .garden(garden)
                    .x(request.getX())
                    .y(request.getY())
                    .plant(plant)
                    .plantedDate(currentDate)
                    .build();
            plantedPlantRepository.save(plantedPlant);
        }
    }

    @Transactional
    public void removePlant(UUID gardenId, Integer x, Integer y) {
        User user = getCurrentUser();
        Garden garden = gardenRepository.findByIdAndUser(gardenId, user)
                .orElseThrow(() -> new RuntimeException("Огород с ID " + gardenId + " не найден"));

        PlantedPlant plantedPlant = plantedPlantRepository.findByGardenAndXAndY(garden, x, y)
                .orElseThrow(() -> new RuntimeException("Растение в клетке (" + x + ", " + y + ") не найдено"));

        plantedPlantRepository.delete(plantedPlant);
    }

    @Transactional
    public void delete(UUID id) {
        User user = getCurrentUser();
        Garden garden = gardenRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Огород с ID " + id + " не найден"));

        plantedPlantRepository.deleteByGarden(garden);
        gardenRepository.delete(garden);
    }

    private void validateCoordinates(GardenType type, Integer x, Integer y) {
        if (type == GardenType.PLOT) {
            if (x < 0 || x >= 8 || y < 0 || y >= 9) {
                throw new IllegalArgumentException("Координаты выходят за границы участка. Участок: 8x9 (x: 0-7, y: 0-8)");
            }
        } else if (type == GardenType.WINDOWSILL) {
            if (x != 0 || y < 0 || y >= 8) {
                throw new IllegalArgumentException("Координаты выходят за границы подоконника. Подоконник: 1x8 (x: 0, y: 0-7)");
            }
        }
    }

    private GardenResponse mapToResponse(Garden garden) {
        List<PlantedPlant> plantedPlants = plantedPlantRepository.findByGarden(garden);
        Map<String, CellInfo> cells = new HashMap<>();

        for (PlantedPlant plantedPlant : plantedPlants) {
            String key = plantedPlant.getX() + "," + plantedPlant.getY();
            CellInfo cellInfo = new CellInfo(
                    plantedPlant.getPlant().getId(),
                    plantedPlant.getPlant().getName(),
                    plantedPlant.getPlantedDate()
            );
            cells.put(key, cellInfo);
        }

        int width, height;
        if (garden.getType() == GardenType.PLOT) {
            width = 8;
            height = 9;
        } else {
            width = 1;
            height = 8;
        }

        return new GardenResponse(
                garden.getId(),
                garden.getType(),
                garden.getName(),
                width,
                height,
                cells
        );
    }
}

