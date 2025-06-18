package com.weatherapp.weather_forecast_api.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWeatherData {
    private LocalDate date;
    private Integer weatherCode;
    private Double minTemperature;
    private Double maxTemperature;
    private Double sunshineDuration;
    private Double pressure;
}