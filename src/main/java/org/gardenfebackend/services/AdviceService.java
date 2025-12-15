package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateAdviceRequest;
import org.gardenfebackend.dtos.requests.UpdateAdviceRequest;
import org.gardenfebackend.dtos.responses.AdviceResponse;
import org.gardenfebackend.models.Advice;
import org.gardenfebackend.models.User;
import org.gardenfebackend.models.UserFavoriteAdvice;
import org.gardenfebackend.repositories.AdviceRepository;
import org.gardenfebackend.repositories.UserFavoriteAdviceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AdviceService {

    private static final String ADVICE_PHOTO_UPLOAD_DIR = "uploads/advicePhotos";

    private final AdviceRepository adviceRepository;
    private final UserFavoriteAdviceRepository userFavoriteAdviceRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new IllegalStateException("Пользователь не аутентифицирован");
    }

    @Transactional
    public AdviceResponse create(CreateAdviceRequest request) {
        String photoUrl = saveAdvicePhoto(request.getPhoto(), null);

        Advice advice = Advice.builder()
                .title(request.getTitle())
                .photoUrl(photoUrl)
                .description(request.getDescription())
                .build();

        Advice savedAdvice = adviceRepository.save(advice);
        return mapToResponse(savedAdvice);
    }

    @Transactional(readOnly = true)
    public List<AdviceResponse> getAll() {
        return adviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdviceResponse getById(UUID id) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + id + " не найден"));
        return mapToResponse(advice);
    }

    @Transactional
    public AdviceResponse update(UUID id, UpdateAdviceRequest request) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + id + " не найден"));

        if (request.getTitle() != null) {
            advice.setTitle(request.getTitle());
        }
        MultipartFile photo = request.getPhoto();
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = saveAdvicePhoto(photo, advice.getPhotoUrl());
            advice.setPhotoUrl(photoUrl);
        }
        if (request.getDescription() != null) {
            advice.setDescription(request.getDescription());
        }

        Advice updatedAdvice = adviceRepository.save(advice);
        return mapToResponse(updatedAdvice);
    }

    @Transactional
    public void delete(UUID id) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + id + " не найден"));
        
        List<UserFavoriteAdvice> favorites = userFavoriteAdviceRepository.findByAdvice(advice);
        userFavoriteAdviceRepository.deleteAll(favorites);
        
        if (advice.getPhotoUrl() != null && !advice.getPhotoUrl().isBlank()) {
            deleteAdvicePhoto(advice.getPhotoUrl());
        }
        
        adviceRepository.delete(advice);
    }

    @Transactional
    public void addToFavorites(UUID adviceId) {
        User user = getCurrentUser();
        Advice advice = adviceRepository.findById(adviceId)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + adviceId + " не найден"));

        userFavoriteAdviceRepository.findByUserAndAdvice(user, advice)
                .ifPresent(favorite -> {
                    throw new IllegalArgumentException("Совет уже добавлен в избранное");
                });

        UserFavoriteAdvice favorite = UserFavoriteAdvice.builder()
                .user(user)
                .advice(advice)
                .build();

        userFavoriteAdviceRepository.save(favorite);
    }

    @Transactional
    public void removeFromFavorites(UUID adviceId) {
        User user = getCurrentUser();
        Advice advice = adviceRepository.findById(adviceId)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + adviceId + " не найден"));

        userFavoriteAdviceRepository.deleteByUserAndAdvice(user, advice);
    }

    @Transactional(readOnly = true)
    public List<AdviceResponse> getFavorites() {
        User user = getCurrentUser();
        return userFavoriteAdviceRepository.findByUser(user).stream()
                .map(favorite -> mapToResponse(favorite.getAdvice()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(UUID adviceId) {
        User user = getCurrentUser();
        Advice advice = adviceRepository.findById(adviceId)
                .orElseThrow(() -> new RuntimeException("Совет с ID " + adviceId + " не найден"));
        
        return userFavoriteAdviceRepository.findByUserAndAdvice(user, advice).isPresent();
    }

    private AdviceResponse mapToResponse(Advice advice) {
        return new AdviceResponse(
                advice.getId(),
                advice.getTitle(),
                advice.getPhotoUrl(),
                advice.getDescription()
        );
    }

    private String saveAdvicePhoto(MultipartFile file, String oldPhotoUrl) {
        try {
            Path uploadDir = Paths.get(ADVICE_PHOTO_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            if (oldPhotoUrl != null && !oldPhotoUrl.isBlank()) {
                deleteAdvicePhoto(oldPhotoUrl);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path target = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), target);

            return "/files/advicePhotos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл фото совета", e);
        }
    }

    private void deleteAdvicePhoto(String photoUrl) {
        try {
            if (photoUrl != null && !photoUrl.isBlank()) {
                String fileName = Paths.get(photoUrl).getFileName().toString();
                Path filePath = Paths.get(ADVICE_PHOTO_UPLOAD_DIR).resolve(fileName);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            System.err.println("Не удалось удалить фото совета: " + photoUrl);
        }
    }
}

