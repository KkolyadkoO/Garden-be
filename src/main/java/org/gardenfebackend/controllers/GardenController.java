package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateGardenRequest;
import org.gardenfebackend.dtos.requests.PlantPlantRequest;
import org.gardenfebackend.dtos.responses.EnumOptionResponse;
import org.gardenfebackend.dtos.responses.GardenResponse;
import org.gardenfebackend.enums.GardenType;
import org.gardenfebackend.services.GardenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gardens")
@RequiredArgsConstructor
public class GardenController {

    private final GardenService gardenService;

    @PostMapping
    public ResponseEntity<GardenResponse> create(@Valid @RequestBody CreateGardenRequest request) {
        GardenResponse response = gardenService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GardenResponse>> getAll() {
        List<GardenResponse> response = gardenService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<EnumOptionResponse>> getGardenTypes() {
        List<EnumOptionResponse> types = Arrays.stream(GardenType.values())
                .map(type -> new EnumOptionResponse(type.name(), type.getLabelRu()))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GardenResponse> getById(@PathVariable UUID id) {
        GardenResponse response = gardenService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/plants")
    public ResponseEntity<Void> plantPlant(
            @PathVariable UUID id,
            @Valid @RequestBody PlantPlantRequest request) {
        gardenService.plantPlant(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/plants")
    public ResponseEntity<Void> removePlant(
            @PathVariable UUID id,
            @RequestParam Integer x,
            @RequestParam Integer y) {
        gardenService.removePlant(id, x, y);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        gardenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

