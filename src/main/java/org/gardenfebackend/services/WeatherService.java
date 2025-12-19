package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.external.OpenWeatherResponse;
import org.gardenfebackend.dtos.responses.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String OPENWEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "2c01d0b81c4f2135d3ff98992bbc2ef0";

    private final RestTemplate restTemplate;

    private static final Map<String, String> WEATHER_TRANSLATIONS = new HashMap<>();

    static {
        WEATHER_TRANSLATIONS.put("Clear", "Ясно");
        WEATHER_TRANSLATIONS.put("Clouds", "Облачно");
        WEATHER_TRANSLATIONS.put("Rain", "Дождь");
        WEATHER_TRANSLATIONS.put("Drizzle", "Моросящий дождь");
        WEATHER_TRANSLATIONS.put("Thunderstorm", "Гроза");
        WEATHER_TRANSLATIONS.put("Snow", "Снег");
        WEATHER_TRANSLATIONS.put("Mist", "Туман");
        WEATHER_TRANSLATIONS.put("Smoke", "Дымка");
        WEATHER_TRANSLATIONS.put("Haze", "Дымка");
        WEATHER_TRANSLATIONS.put("Dust", "Пыль");
        WEATHER_TRANSLATIONS.put("Fog", "Туман");
        WEATHER_TRANSLATIONS.put("Sand", "Песчаная буря");
        WEATHER_TRANSLATIONS.put("Ash", "Пепел");
        WEATHER_TRANSLATIONS.put("Squall", "Шквал");
        WEATHER_TRANSLATIONS.put("Tornado", "Торнадо");
    }

    public WeatherResponse getWeatherByCoordinates(Double lat, Double lon) {
        String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                OPENWEATHER_API_URL, lat, lon, API_KEY);

        return fetchAndProcessWeather(url);
    }

    private WeatherResponse fetchAndProcessWeather(String url) {
        try {
            ResponseEntity<OpenWeatherResponse> response = restTemplate.getForEntity(url, OpenWeatherResponse.class);
            OpenWeatherResponse weatherData = response.getBody();

            if (weatherData == null || weatherData.getWeather() == null || weatherData.getWeather().isEmpty()) {
                throw new RuntimeException("Не удалось получить данные о погоде");
            }

            OpenWeatherResponse.Weather weather = weatherData.getWeather().get(0);
            String weatherMain = weather.getMain();
            String weatherName = translateWeather(weatherMain);
            String photoUrl = "/files/weather/" + weatherMain + ".png";

            Double dayTemperature = weatherData.getMain() != null ? weatherData.getMain().getTempMax() : null;
            Double nightTemperature = weatherData.getMain() != null ? weatherData.getMain().getTempMin() : null;

            return new WeatherResponse(
                    nightTemperature,
                    dayTemperature,
                    weatherName,
                    photoUrl
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении данных о погоде: " + e.getMessage(), e);
        }
    }

    private String translateWeather(String weatherMain) {
        return WEATHER_TRANSLATIONS.getOrDefault(weatherMain, weatherMain);
    }
}

