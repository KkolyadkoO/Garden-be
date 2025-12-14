package org.gardenfebackend.dtos.requests;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePlantRequest {

    private String type;

    private String name;

    private MultipartFile photo;

    private String sunHours;

    private String temperature;

    @Positive(message = "Количество дней между поливами должно быть положительным")
    private Integer wateringDays;

    private String goodNeighbours;

    private String badNeighbours;

    private String description;

    private Boolean isVerified;
}

