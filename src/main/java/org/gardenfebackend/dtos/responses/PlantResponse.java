package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gardenfebackend.enums.PlantType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantResponse {

    private UUID id;
    private PlantType type;
    private boolean isVerified;
    private String name;
    private String photoUrl;
    private String sunHours;
    private String temperature;
    private Integer wateringDays;
    private String goodNeighbours;
    private String badNeighbours;
    private String description;
}

