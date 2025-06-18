package com.weatherapp.weather_forecast_api.controller;

import com.weatherapp.weather_forecast_api.dto.LocationRequest;
import com.weatherapp.weather_forecast_api.dto.WeatherForecastResponse;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/forecast")
    public ResponseEntity<WeatherForecastResponse> getWeatherForecast(@Valid @RequestBody LocationRequest locationRequest) {
        WeatherForecastResponse response = weatherService.getWeatherForecast(locationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/summary")
    public ResponseEntity<WeatherSummaryResponse> getWeatherSummary(@Valid @RequestBody LocationRequest locationRequest) {
        WeatherSummaryResponse response = weatherService.getWeatherSummary(locationRequest);
        return ResponseEntity.ok(response);
    }
}
