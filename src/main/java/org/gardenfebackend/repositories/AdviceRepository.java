package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Advice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdviceRepository extends JpaRepository<Advice, UUID> {
}

