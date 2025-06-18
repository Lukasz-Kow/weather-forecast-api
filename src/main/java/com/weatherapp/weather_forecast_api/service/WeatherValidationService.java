package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import org.springframework.stereotype.Service;

@Service
public class WeatherValidationService {

    public void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new ValidationException("Latitude and longitude cannot be null");
        }

        if (latitude < -90.0 || latitude > 90.0) {
            throw new ValidationException("Latitude must be between -90.0 and 90.0");
        }

        if (longitude < -180.0 || longitude > 180.0) {
            throw new ValidationException("Longitude must be between -180.0 and 180.0");
        }
    }

    public void validateResponse(OpenMeteo response) {
        if (response == null) {
            throw new WeatherApiException("No response received from Open-Meteo API");
        }

        if (response.getDaily() == null) {
            throw new WeatherApiException("No daily weather data received from Open-Meteo API");
        }

        if (response.getDaily().getTime() == null || response.getDaily().getTime().isEmpty()) {
            throw new WeatherApiException("No time data received from API");
        }
    }
}
