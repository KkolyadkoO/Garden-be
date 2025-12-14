package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PlantPlantRequest {

    @NotNull(message = "ID растения обязателен")
    private UUID plantId;

    @NotNull(message = "Координата X обязательна")
    private Integer x;

    @NotNull(message = "Координата Y обязательна")
    private Integer y;
}

