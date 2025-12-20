package org.gardenfebackend.repositories;

import org.gardenfebackend.models.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, UUID> { }
