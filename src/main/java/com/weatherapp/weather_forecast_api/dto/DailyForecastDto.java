package com.weatherapp.weather_forecast_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record DailyForecastDto(
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate date,
        Integer weatherCode,
        Double minTemperature,
        Double maxTemperature,
        Double energyGenerated,
        Double pressure
) {
}
