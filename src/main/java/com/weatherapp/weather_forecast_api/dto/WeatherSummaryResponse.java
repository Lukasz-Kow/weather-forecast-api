package com.weatherapp.weather_forecast_api.dto;

public record WeatherSummaryResponse(
        Double averagePressure,
        Double averageSunshineHours,
        Double minTemperature,
        Double maxTemperature,
        String weekSummary
) {
}
