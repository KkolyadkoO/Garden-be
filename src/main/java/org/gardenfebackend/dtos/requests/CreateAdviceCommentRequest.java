package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAdviceCommentRequest {

    @NotNull
    private UUID adviceId;

    @NotBlank
    private String text;
}
