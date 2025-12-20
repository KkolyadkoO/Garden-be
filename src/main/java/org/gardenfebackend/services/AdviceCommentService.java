package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateAdviceCommentRequest;
import org.gardenfebackend.dtos.responses.AdviceCommentResponse;
import org.gardenfebackend.models.Advice;
import org.gardenfebackend.models.AdviceComment;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.AdviceCommentRepository;
import org.gardenfebackend.repositories.AdviceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdviceCommentService {

    private final AdviceRepository adviceRepository;
    private final AdviceCommentRepository commentRepository;

    @Transactional
    public AdviceCommentResponse addComment(CreateAdviceCommentRequest request) {
        Advice advice = adviceRepository.findById(request.getAdviceId())
                .orElseThrow(() -> new IllegalArgumentException("Advice not found"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AdviceComment comment = AdviceComment.builder()
                .advice(advice)
                .user(user)
                .text(request.getText())
                .build();

        AdviceComment saved = commentRepository.save(comment);

        return new AdviceCommentResponse(
                saved.getId(),
                saved.getText(),
                saved.getCreatedAt(),
                user.getFullName(),
                user.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public List<AdviceCommentResponse> getComments(UUID adviceId) {
        Advice advice = adviceRepository.findById(adviceId)
                .orElseThrow(() -> new IllegalArgumentException("Advice not found"));

        return commentRepository.findByAdviceOrderByCreatedAtDesc(advice)
                .stream()
                .map(comment -> new AdviceCommentResponse(
                        comment.getId(),
                        comment.getText(),
                        comment.getCreatedAt(),
                        comment.getUser().getFullName(),
                        comment.getUser().getEmail()
                ))
                .toList();
    }
}
