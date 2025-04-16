package com.weather.controller;

import com.weather.model.WeatherData;
import com.weather.service.WeatherDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// RestController: Enables handling HTTP requests and returning JSON responses.
// RequestMapping: specifies Base URL for all endpoints in the controller. e.g. an endpoint defined with @GetMapping("/{city}") will be accessible at /api/weather/{city}
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherDataService weatherDataService;

    public WeatherController(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String city) {
        WeatherData weatherData = weatherDataService.getLatestData(city);
        
        if (weatherData == null) {
            return ResponseEntity.ok().body("{\"message\": \"No data available for " + city + "\"}");
        }
        
        return ResponseEntity.ok(weatherData);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllWeatherData() {
        return ResponseEntity.ok(weatherDataService.getAllData());
    }
}