package org.gardenfebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.responses.WeatherResponse;
import org.gardenfebackend.services.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/coordinates")
    public ResponseEntity<WeatherResponse> getWeatherByCoordinates(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        WeatherResponse response = weatherService.getWeatherByCoordinates(lat, lon);
        return ResponseEntity.ok(response);
    }
}

