package org.gardenfebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CheckEmailRequest;
import org.gardenfebackend.dtos.requests.UpdateProfileRequest;
import org.gardenfebackend.dtos.responses.UserProfileResponse;
import org.gardenfebackend.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        var result = userService.getProfile();
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfile(UpdateProfileRequest request) {
        userService.updateProfile(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody CheckEmailRequest request) {
        boolean exists = userService.existsByEmail(request);
        return ResponseEntity.ok(exists);
    }
}


