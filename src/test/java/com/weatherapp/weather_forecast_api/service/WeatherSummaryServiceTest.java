package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class WeatherSummaryServiceTest {

    private WeatherSummaryService weatherSummaryService;

    @BeforeEach
    void setUp() {
        weatherSummaryService = new WeatherSummaryService();
    }

    @Test
    void shouldCalculateAveragePressure() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 1, 12.1, 29.0, 15.32, 1010.2)
        );

        double result = weatherSummaryService.calculateAveragePressure(weatherData);

        Assertions.assertThat(result).isEqualTo(1007.93);
    }

    @Test
    void shouldCalculateAverageSunshineHours() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 1, 12.1, 29.0, 15.32, 1010.2)
        );

        double result = weatherSummaryService.calculateAverageSunshineHours(weatherData);

        Assertions.assertThat(result).isEqualTo(11.57);
    }

    @Test
    void shouldFindMinTemperature() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 1, 12.1, 29.0, 15.32, 1010.2)
        );

        double result = weatherSummaryService.findMinTemperature(weatherData);

        Assertions.assertThat(result).isEqualTo(12.1);
    }

    @Test
    void shouldFindMaxTemperature() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 1, 12.1, 29.0, 15.32, 1010.2)
        );

        double result = weatherSummaryService.findMaxTemperature(weatherData);

        Assertions.assertThat(result).isEqualTo(29.0);
    }

    @Test
    void shouldGenerateWeekSummaryWithRain() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 51, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 61, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 71, 12.1, 29.0, 15.32, 1010.2),
                new DailyWeatherData(LocalDate.of(2025, 6, 20), 80, 10.5, 20.1, 5.2, 1012.1),
                new DailyWeatherData(LocalDate.of(2025, 6, 21), 1, 15.2, 24.3, 12.1, 1009.8),
                new DailyWeatherData(LocalDate.of(2025, 6, 22), 3, 18.1, 26.7, 9.8, 1007.5),
                new DailyWeatherData(LocalDate.of(2025, 6, 23), 2, 16.8, 23.9, 11.2, 1008.9)
        );

        String result = weatherSummaryService.generateWeekSummary(weatherData);

        Assertions.assertThat(result).isEqualTo("z opadami");
    }

    @Test
    void shouldGenerateWeekSummaryWithoutRain() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 0, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 1, 16.7, 25.8, 10.63, 1005.6),
                new DailyWeatherData(LocalDate.of(2025, 6, 19), 2, 12.1, 29.0, 15.32, 1010.2),
                new DailyWeatherData(LocalDate.of(2025, 6, 20), 3, 10.5, 20.1, 5.2, 1012.1),
                new DailyWeatherData(LocalDate.of(2025, 6, 21), 1, 15.2, 24.3, 12.1, 1009.8),
                new DailyWeatherData(LocalDate.of(2025, 6, 22), 51, 18.1, 26.7, 9.8, 1007.5),
                new DailyWeatherData(LocalDate.of(2025, 6, 23), 2, 16.8, 23.9, 11.2, 1008.9)
        );

        String result = weatherSummaryService.generateWeekSummary(weatherData);

        Assertions.assertThat(result).isEqualTo("bez opadów");
    }

    @Test
    void shouldReturnZeroWhenEmptyList() {
        List<DailyWeatherData> emptyData = List.of();

        double avgPressure = weatherSummaryService.calculateAveragePressure(emptyData);
        double avgSunshine = weatherSummaryService.calculateAverageSunshineHours(emptyData);
        double minTemp = weatherSummaryService.findMinTemperature(emptyData);
        double maxTemp = weatherSummaryService.findMaxTemperature(emptyData);

        Assertions.assertThat(avgPressure).isEqualTo(0.0);
        Assertions.assertThat(avgSunshine).isEqualTo(0.0);
        Assertions.assertThat(minTemp).isEqualTo(0.0);
        Assertions.assertThat(maxTemp).isEqualTo(0.0);
    }
}
