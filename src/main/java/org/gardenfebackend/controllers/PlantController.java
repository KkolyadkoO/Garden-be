package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreatePlantRequest;
import org.gardenfebackend.dtos.requests.UpdatePlantRequest;
import org.gardenfebackend.dtos.responses.PlantResponse;
import org.gardenfebackend.enums.UserRole;
import org.gardenfebackend.models.User;
import org.gardenfebackend.services.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponse> create(@Valid @ModelAttribute CreatePlantRequest request) {
        PlantResponse response = plantService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlantResponse>> getAll() {
        List<PlantResponse> response = plantService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantResponse> getById(@PathVariable UUID id) {
        PlantResponse response = plantService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verified/{isVerified}")
    public ResponseEntity<List<PlantResponse>> getByIsVerified(@PathVariable boolean isVerified) {
        List<PlantResponse> response = plantService.getByIsVerified(isVerified);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponse> update(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdatePlantRequest request) {
        checkAdminAccess();
        PlantResponse response = plantService.update(id, request);
        return ResponseEntity.ok(response);
    }

    private void checkAdminAccess() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            if (user.getRole() != UserRole.ADMIN) {
                throw new AccessDeniedException("Доступ запрещен. Требуется роль администратора.");
            }
        } else {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
    }
}

