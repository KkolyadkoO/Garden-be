package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.AuthRequest;
import org.gardenfebackend.dtos.requests.RegisterRequest;
import org.gardenfebackend.dtos.requests.RefreshTokenRequest;
import org.gardenfebackend.dtos.responses.AuthResponse;
import org.gardenfebackend.enums.UserRole;
import org.gardenfebackend.models.RefreshToken;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.UserRepository;
import org.gardenfebackend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public User register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        userRepository.findByEmail(normalizedEmail)
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Пользователь с таким email уже существует");
                });

        User user = User.builder()
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = token.getUser();

        // вращаем refresh-токен: текущий помечаем как отозванный и создаём новый
        refreshTokenService.revokeRefreshToken(token.getToken());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String newAccessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        // можно отозвать конкретный refresh-токен
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }
}
