package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateAdviceRequest;
import org.gardenfebackend.dtos.requests.UpdateAdviceRequest;
import org.gardenfebackend.dtos.responses.AdviceResponse;
import org.gardenfebackend.enums.UserRole;
import org.gardenfebackend.models.User;
import org.gardenfebackend.services.AdviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/advices")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService adviceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdviceResponse> create(@Valid @ModelAttribute CreateAdviceRequest request) {
        checkAdminAccess();
        AdviceResponse response = adviceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdviceResponse>> getAll() {
        List<AdviceResponse> response = adviceService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdviceResponse> getById(@PathVariable UUID id) {
        AdviceResponse response = adviceService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdviceResponse> update(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateAdviceRequest request) {
        checkAdminAccess();
        AdviceResponse response = adviceService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        checkAdminAccess();
        adviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> addToFavorites(@PathVariable UUID id) {
        adviceService.addToFavorites(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable UUID id) {
        adviceService.removeFromFavorites(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<AdviceResponse>> getFavorites() {
        List<AdviceResponse> response = adviceService.getFavorites();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/favorite")
    public ResponseEntity<Boolean> isFavorite(@PathVariable UUID id) {
        boolean isFavorite = adviceService.isFavorite(id);
        return ResponseEntity.ok(isFavorite);
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

