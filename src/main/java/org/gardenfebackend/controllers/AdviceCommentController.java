package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.CreateAdviceCommentRequest;
import org.gardenfebackend.dtos.responses.AdviceCommentResponse;
import org.gardenfebackend.services.AdviceCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/advices/comments")
@RequiredArgsConstructor
public class AdviceCommentController {

    private final AdviceCommentService commentService;

    @PostMapping
    public ResponseEntity<AdviceCommentResponse> addComment(
            @RequestBody @Valid CreateAdviceCommentRequest request) {
        var comment = commentService.addComment(request);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{adviceId}")
    public ResponseEntity<List<AdviceCommentResponse>> listComments(@PathVariable UUID adviceId) {
        return ResponseEntity.ok(commentService.getComments(adviceId));
    }
}
