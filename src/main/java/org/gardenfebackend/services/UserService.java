package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CheckEmailRequest;
import org.gardenfebackend.dtos.requests.UpdateProfileRequest;
import org.gardenfebackend.dtos.responses.UserProfileResponse;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String AVATAR_UPLOAD_DIR = "uploads/avatars";

    private final UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new IllegalStateException("Пользователь не аутентифицирован");
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile() {
        User user = getCurrentUser();
        return new UserProfileResponse(
                user.getEmail(),
                user.getFullName(),
                user.getProfilePhotoUrl()
        );
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(CheckEmailRequest request) {
        return userRepository.findByEmail(request.getEmail()).isPresent();
    }

    @Transactional
    public void updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        MultipartFile profilePhoto = request.getProfilePhoto();
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            String avatarUrl = saveAvatar(profilePhoto, user.getProfilePhotoUrl());
            user.setProfilePhotoUrl(avatarUrl);
        }

        userRepository.save(user);
    }

    private String saveAvatar(MultipartFile file, String oldPhotoUrl) {
        try {
            Path uploadDir = Paths.get(AVATAR_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            if (oldPhotoUrl != null && !oldPhotoUrl.isBlank()) {
                String oldFileName = Paths.get(oldPhotoUrl).getFileName().toString();
                Path oldFilePath = uploadDir.resolve(oldFileName);
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException ex) {
                    System.err.println("Не удалось удалить старый аватар: " + oldFilePath);
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

            return "/files/avatars/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл аватара", e);
        }
    }
}

