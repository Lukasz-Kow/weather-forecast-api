package com.weatherapp.weather_forecast_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weather.api")
@Data
public class ApiConfig {
    private String baseUrl = "https://api.open-meteo.com";
    private String forecastPath = "/v1/forecast";
    private String dailyParams = "temperature_2m_max,temperature_2m_min,weather_code,surface_pressure_mean,sunshine_duration";
    private String timezone = "auto";
    private Integer forecastDays = 7;
}