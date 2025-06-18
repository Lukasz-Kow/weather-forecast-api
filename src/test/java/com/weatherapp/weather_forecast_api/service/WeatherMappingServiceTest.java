package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.dto.DailyForecastDto;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherMappingServiceTest {

    @Mock
    private SolarEnergyService solarEnergyService;

    @Mock
    private WeatherSummaryService weatherSummaryService;

    private WeatherMappingService weatherMappingService;

    @BeforeEach
    void setUp() {
        weatherMappingService = new WeatherMappingService(solarEnergyService);
    }

    @Test
    void shouldMapToForecastDtos() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6)
        );

        when(solarEnergyService.calculateEnergyProduction(8.76)).thenReturn(4.38);
        when(solarEnergyService.calculateEnergyProduction(10.63)).thenReturn(5.32);

        List<DailyForecastDto> result = weatherMappingService.mapToForecastDtos(weatherData);

        Assertions.assertThat(result).hasSize(2);

        DailyForecastDto firstDay = result.get(0);
        Assertions.assertThat(firstDay.date()).isEqualTo(LocalDate.of(2025, 6, 17));
        Assertions.assertThat(firstDay.weatherCode()).isEqualTo(3);
        Assertions.assertThat(firstDay.minTemperature()).isEqualTo(14.3);
        Assertions.assertThat(firstDay.maxTemperature()).isEqualTo(22.8);
        Assertions.assertThat(firstDay.energyGenerated()).isEqualTo(4.38);
        Assertions.assertThat(firstDay.pressure()).isEqualTo(1008.0);

        verify(solarEnergyService).calculateEnergyProduction(8.76);
        verify(solarEnergyService).calculateEnergyProduction(10.63);
    }

    @Test
    void shouldMapToSummaryResponse() {
        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0)
        );

        when(weatherSummaryService.calculateAveragePressure(weatherData)).thenReturn(1008.0);
        when(weatherSummaryService.calculateAverageSunshineHours(weatherData)).thenReturn(8.76);
        when(weatherSummaryService.findMinTemperature(weatherData)).thenReturn(14.3);
        when(weatherSummaryService.findMaxTemperature(weatherData)).thenReturn(22.8);
        when(weatherSummaryService.generateWeekSummary(weatherData)).thenReturn("bez opadów");

        WeatherSummaryResponse result = weatherMappingService.mapToSummaryResponse(weatherData, weatherSummaryService);

        Assertions.assertThat(result.averagePressure()).isEqualTo(1008.0);
        Assertions.assertThat(result.averageSunshineHours()).isEqualTo(8.76);
        Assertions.assertThat(result.minTemperature()).isEqualTo(14.3);
        Assertions.assertThat(result.maxTemperature()).isEqualTo(22.8);
        Assertions.assertThat(result.weekSummary()).isEqualTo("bez opadów");

        verify(weatherSummaryService).calculateAveragePressure(weatherData);
        verify(weatherSummaryService).calculateAverageSunshineHours(weatherData);
        verify(weatherSummaryService).findMinTemperature(weatherData);
        verify(weatherSummaryService).findMaxTemperature(weatherData);
        verify(weatherSummaryService).generateWeekSummary(weatherData);
    }

    @Test
    void shouldHandleEmptyWeatherDataList() {
        List<DailyWeatherData> emptyData = List.of();

        List<DailyForecastDto> result = weatherMappingService.mapToForecastDtos(emptyData);

        Assertions.assertThat(result).isEmpty();
        verifyNoInteractions(solarEnergyService);
    }
}
