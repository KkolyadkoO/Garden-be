package org.gardenfebackend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TelegramAuthRequest {

    @NotBlank
    private String tgAuthResult;
}
