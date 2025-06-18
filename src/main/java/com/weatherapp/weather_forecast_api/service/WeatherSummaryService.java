package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class WeatherSummaryService {

    public double calculateAveragePressure(List<DailyWeatherData> weatherData) {
        OptionalDouble average = weatherData.stream()
                .mapToDouble(DailyWeatherData::getPressure)
                .average();

        double result = average.orElse(0.0);
        return Math.round(result * 100.0) / 100.0;
    }

    public double calculateAverageSunshineHours(List<DailyWeatherData> weatherData) {
        OptionalDouble average = weatherData.stream()
                .mapToDouble(DailyWeatherData::getSunshineDuration)
                .average();

        double result = average.orElse(0.0);
        return Math.round(result * 100.0) / 100.0;
    }

    public double findMinTemperature(List<DailyWeatherData> weatherData) {
        OptionalDouble min = weatherData.stream()
                .mapToDouble(DailyWeatherData::getMinTemperature)
                .min();

        return min.orElse(0.0);
    }

    public double findMaxTemperature(List<DailyWeatherData> weatherData) {
        OptionalDouble max = weatherData.stream()
                .mapToDouble(DailyWeatherData::getMaxTemperature)
                .max();

        return max.orElse(0.0);
    }

    public String generateWeekSummary(List<DailyWeatherData> weatherData) {
        long rainyDays = weatherData.stream()
                .mapToInt(DailyWeatherData::getWeatherCode)
                .filter(this::isRainyWeatherCode)
                .count();

        return rainyDays >= 4 ? "z opadami" : "bez opadów";
    }

    private boolean isRainyWeatherCode(int weatherCode) {
        return (weatherCode >= 51 && weatherCode <= 67) ||
                (weatherCode >= 71 && weatherCode <= 77) ||
                (weatherCode >= 80 && weatherCode <= 99);
    }
}
