package org.gardenfebackend.repositories;

import org.gardenfebackend.models.Advice;
import org.gardenfebackend.models.AdviceComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdviceCommentRepository extends JpaRepository<AdviceComment, UUID> {
    List<AdviceComment> findByAdviceOrderByCreatedAtDesc(Advice advice);
}
