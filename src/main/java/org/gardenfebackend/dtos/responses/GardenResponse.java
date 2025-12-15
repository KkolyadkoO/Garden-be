package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gardenfebackend.enums.GardenType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GardenResponse {

    private UUID id;
    private GardenType type;
    private String name;
    private Integer width;
    private Integer height;
    private Map<String, CellInfo> cells;
}

