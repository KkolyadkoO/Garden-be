package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupportMessageRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String message;
}
