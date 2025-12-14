package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellInfo {
    private UUID plantId;
    private String plantName;
    private LocalDate plantedDate;
}

