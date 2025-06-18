package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.dto.DailyForecastDto;
import com.weatherapp.weather_forecast_api.dto.LocationRequest;
import com.weatherapp.weather_forecast_api.dto.WeatherForecastResponse;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {
    private final OpenMeteoService openMeteoService;
    private final WeatherSummaryService weatherSummaryService;
    private final WeatherMappingService weatherMappingService;

    @Autowired
    public WeatherService(OpenMeteoService openMeteoService,
                          WeatherSummaryService weatherSummaryService,
                          WeatherMappingService weatherMappingService) {
        this.openMeteoService = openMeteoService;
        this.weatherSummaryService = weatherSummaryService;
        this.weatherMappingService = weatherMappingService;
    }

    public WeatherForecastResponse getWeatherForecast(LocationRequest locationRequest) {
        validateLocationRequest(locationRequest);

        List<DailyWeatherData> weatherData = openMeteoService.getWeatherForecast(
                locationRequest.latitude(), locationRequest.longitude());

        List<DailyForecastDto> forecastDtos = weatherMappingService.mapToForecastDtos(weatherData);

        return new WeatherForecastResponse(forecastDtos);
    }

    public WeatherSummaryResponse getWeatherSummary(LocationRequest locationRequest) {
        validateLocationRequest(locationRequest);

        List<DailyWeatherData> weatherData = openMeteoService.getWeatherForecast(
                locationRequest.latitude(), locationRequest.longitude());

        return weatherMappingService.mapToSummaryResponse(weatherData, weatherSummaryService);
    }

    private void validateLocationRequest(LocationRequest locationRequest) {
        if (locationRequest.latitude() == null || locationRequest.longitude() == null) {
            throw new ValidationException("Latitude and longitude are required");
        }
    }
}
