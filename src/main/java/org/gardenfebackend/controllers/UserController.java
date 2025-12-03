package org.gardenfebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.UpdateProfileRequest;
import org.gardenfebackend.dtos.responses.UserProfileResponse;
import org.gardenfebackend.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateProfile(UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
}


