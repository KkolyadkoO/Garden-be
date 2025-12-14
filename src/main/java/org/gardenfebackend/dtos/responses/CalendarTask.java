package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarTask {

    private UUID gardenId;
    private String gardenName;
    private UUID plantId;
    private String plantName;
    private Integer x;
    private Integer y;
}

