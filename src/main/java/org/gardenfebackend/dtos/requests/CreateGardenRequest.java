package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gardenfebackend.enums.GardenType;

@Data
public class CreateGardenRequest {

    @NotNull(message = "Тип огорода обязателен")
    private GardenType type;

    @NotBlank(message = "Название огорода обязательно")
    private String name;
}

