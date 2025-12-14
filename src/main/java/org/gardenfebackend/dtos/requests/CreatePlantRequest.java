package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.gardenfebackend.enums.PlantType;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatePlantRequest {

    @NotBlank(message = "Тип растения обязателен")
    private String type;

    @NotBlank(message = "Название растения обязательно")
    private String name;

    @NotNull(message = "Фотография обязательна")
    private MultipartFile photo;

    @NotBlank(message = "Часы солнечного света обязательны")
    private String sunHours;

    @NotBlank(message = "Температура обязательна")
    private String temperature;

    @NotNull(message = "Количество дней между поливами обязательно")
    @Positive(message = "Количество дней между поливами должно быть положительным")
    private Integer wateringDays;

    @NotBlank(message = "Хорошие соседи обязательны")
    private String goodNeighbours;

    @NotBlank(message = "Плохие соседи обязательны")
    private String badNeighbours;

    private String description;
}

