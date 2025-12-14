package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Advice;
import org.gardenfebackend.models.User;
import org.gardenfebackend.models.UserFavoriteAdvice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFavoriteAdviceRepository extends JpaRepository<UserFavoriteAdvice, UUID> {
    Optional<UserFavoriteAdvice> findByUserAndAdvice(User user, Advice advice);
    List<UserFavoriteAdvice> findByUser(User user);
    List<UserFavoriteAdvice> findByAdvice(Advice advice);
    void deleteByUserAndAdvice(User user, Advice advice);
}

