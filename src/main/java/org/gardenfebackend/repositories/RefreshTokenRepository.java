package org.gardenfebackend.repositories;

import org.gardenfebackend.models.RefreshToken;
import org.gardenfebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    long deleteByUser(User user);

    void deleteByExpiresAtBefore(Instant time);
}


