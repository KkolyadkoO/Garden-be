package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private Double nightTemperature;
    private Double dayTemperature;
    private String weatherName;
    private String photoUrl;
}

