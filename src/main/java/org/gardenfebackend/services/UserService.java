package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
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

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        MultipartFile profilePhoto = request.getProfilePhoto();
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            String avatarUrl = saveAvatar(profilePhoto);
            user.setProfilePhotoUrl(avatarUrl);
        }

        User saved = userRepository.save(user);

        return new UserProfileResponse(
                saved.getEmail(),
                saved.getFullName(),
                saved.getProfilePhotoUrl()
        );
    }

    private String saveAvatar(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(AVATAR_UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path target = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), target);

            // URL по которому можно получить файл
            return "/files/avatars/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл аватара", e);
        }
    }
}

