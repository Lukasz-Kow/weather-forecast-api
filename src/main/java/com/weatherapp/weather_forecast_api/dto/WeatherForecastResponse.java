package com.weatherapp.weather_forecast_api.dto;

import java.util.List;

public record WeatherForecastResponse(
        List<DailyForecastDto> forecast
) {}
