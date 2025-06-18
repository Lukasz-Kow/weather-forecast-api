package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.dto.DailyForecastDto;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherMappingService {
    private final SolarEnergyService solarEnergyService;

    @Autowired
    public WeatherMappingService(SolarEnergyService solarEnergyService) {
        this.solarEnergyService = solarEnergyService;
    }

    public List<DailyForecastDto> mapToForecastDtos(List<DailyWeatherData> weatherData) {
        return weatherData.stream()
                .map(this::mapToDailyForecastDto)
                .toList();
    }

    public WeatherSummaryResponse mapToSummaryResponse(List<DailyWeatherData> weatherData,
                                                       WeatherSummaryService summaryService) {
        return new WeatherSummaryResponse(
                summaryService.calculateAveragePressure(weatherData),
                summaryService.calculateAverageSunshineHours(weatherData),
                summaryService.findMinTemperature(weatherData),
                summaryService.findMaxTemperature(weatherData),
                summaryService.generateWeekSummary(weatherData)
        );
    }

    private DailyForecastDto mapToDailyForecastDto(DailyWeatherData weatherData) {
        double energyGenerated = solarEnergyService.calculateEnergyProduction(
                weatherData.getSunshineDuration());

        return new DailyForecastDto(
                weatherData.getDate(),
                weatherData.getWeatherCode(),
                weatherData.getMinTemperature(),
                weatherData.getMaxTemperature(),
                energyGenerated,
                weatherData.getPressure()
        );
    }
}
