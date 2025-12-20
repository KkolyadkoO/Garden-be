package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.converters.TelegramAuthUtils;
import org.gardenfebackend.dtos.TelegramAuthRequest;
import org.gardenfebackend.dtos.requests.AuthRequest;
import org.gardenfebackend.dtos.requests.CheckEmailRequest;
import org.gardenfebackend.dtos.requests.RefreshTokenRequest;
import org.gardenfebackend.dtos.requests.RegisterRequest;
import org.gardenfebackend.dtos.responses.AuthResponse;
import org.gardenfebackend.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@Valid @RequestBody CheckEmailRequest request) {
        boolean exists = authService.existsByEmail(request.getEmail());
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/google-mobile")
    public ResponseEntity<AuthResponse> googleMobileLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        AuthResponse response = authService.googleMobileLogin(idToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/telegram")
    public ResponseEntity<AuthResponse> telegramLogin(@RequestBody @Valid TelegramAuthRequest request) {
        Map<String, String> telegramData = TelegramAuthUtils.decodeBase64JsonToMap(request.getTgAuthResult());
        AuthResponse response = authService.telegramLogin(telegramData);
        return ResponseEntity.ok(response);
    }
}
