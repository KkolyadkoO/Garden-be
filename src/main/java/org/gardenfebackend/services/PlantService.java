package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreatePlantRequest;
import org.gardenfebackend.dtos.requests.UpdatePlantRequest;
import org.gardenfebackend.dtos.responses.PlantResponse;
import org.gardenfebackend.enums.PlantType;
import org.gardenfebackend.models.Plant;
import org.gardenfebackend.repositories.PlantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {

    private static final String PLANT_PHOTO_UPLOAD_DIR = "uploads/plantFotos";

    private final PlantRepository plantRepository;

    @Transactional
    public PlantResponse create(CreatePlantRequest request) {
        String photoUrl = savePlantPhoto(request.getPhoto(), null);
        PlantType plantType = parsePlantType(request.getType());

        Plant plant = Plant.builder()
                .type(plantType)
                .name(request.getName())
                .photoUrl(photoUrl)
                .sunHours(request.getSunHours())
                .temperature(request.getTemperature())
                .wateringDays(request.getWateringDays())
                .goodNeighbours(request.getGoodNeighbours())
                .badNeighbours(request.getBadNeighbours())
                .description(request.getDescription())
                .isVerified(false)
                .build();

        Plant savedPlant = plantRepository.save(plant);
        return mapToResponse(savedPlant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponse> getAll() {
        return plantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantResponse getById(UUID id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Растение с ID " + id + " не найдено"));
        return mapToResponse(plant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponse> getByIsVerified(boolean isVerified) {
        return plantRepository.findByIsVerified(isVerified).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlantResponse update(UUID id, UpdatePlantRequest request) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Растение с ID " + id + " не найдено"));

        if (request.getType() != null && !request.getType().isBlank()) {
            PlantType plantType = parsePlantType(request.getType());
            plant.setType(plantType);
        }
        if (request.getName() != null) {
            plant.setName(request.getName());
        }
        MultipartFile photo = request.getPhoto();
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = savePlantPhoto(photo, plant.getPhotoUrl());
            plant.setPhotoUrl(photoUrl);
        }
        if (request.getSunHours() != null) {
            plant.setSunHours(request.getSunHours());
        }
        if (request.getTemperature() != null) {
            plant.setTemperature(request.getTemperature());
        }
        if (request.getWateringDays() != null) {
            plant.setWateringDays(request.getWateringDays());
        }
        if (request.getGoodNeighbours() != null) {
            plant.setGoodNeighbours(request.getGoodNeighbours());
        }
        if (request.getBadNeighbours() != null) {
            plant.setBadNeighbours(request.getBadNeighbours());
        }
        if (request.getDescription() != null) {
            plant.setDescription(request.getDescription());
        }
        if (request.getIsVerified() != null) {
            plant.setVerified(request.getIsVerified());
        }

        Plant updatedPlant = plantRepository.save(plant);
        return mapToResponse(updatedPlant);
    }

    private PlantResponse mapToResponse(Plant plant) {
        return new PlantResponse(
                plant.getId(),
                plant.getType(),
                plant.isVerified(),
                plant.getName(),
                plant.getPhotoUrl(),
                plant.getSunHours(),
                plant.getTemperature(),
                plant.getWateringDays(),
                plant.getGoodNeighbours(),
                plant.getBadNeighbours(),
                plant.getDescription()
        );
    }

    private String savePlantPhoto(MultipartFile file, String oldPhotoUrl) {
        try {
            Path uploadDir = Paths.get(PLANT_PHOTO_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            if (oldPhotoUrl != null && !oldPhotoUrl.isBlank()) {
                String oldFileName = Paths.get(oldPhotoUrl).getFileName().toString();
                Path oldFilePath = uploadDir.resolve(oldFileName);
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException ex) {
                    System.err.println("Не удалось удалить старое фото растения: " + oldFilePath);
                }
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path target = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), target);

            return "/files/plantFotos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл фото растения", e);
        }
    }

    private PlantType parsePlantType(String typeString) {
        if (typeString == null || typeString.isBlank()) {
            throw new IllegalArgumentException("Тип растения не может быть пустым");
        }
        try {
            return PlantType.valueOf(typeString.trim());
        } catch (IllegalArgumentException e) {
            for (PlantType type : PlantType.values()) {
                if (type.name().equalsIgnoreCase(typeString.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип растения: " + typeString + ". Допустимые значения: Flower, Berry, Vegetable, Tree");
        }
    }
}

