package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdviceCommentResponse {
    private UUID id;
    private String text;
    private Instant createdAt;
    private String userName;
    private String userEmail;
}
