package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.dto.DailyForecastDto;
import com.weatherapp.weather_forecast_api.dto.LocationRequest;
import com.weatherapp.weather_forecast_api.dto.WeatherForecastResponse;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.exception.ValidationException;
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
class WeatherServiceTest {

    @Mock
    private OpenMeteoService openMeteoService;

    @Mock
    private WeatherSummaryService weatherSummaryService;

    @Mock
    private WeatherMappingService weatherMappingService;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(
                openMeteoService,
                weatherSummaryService,
                weatherMappingService
        );
    }

    @Test
    void shouldGetWeatherForecast() {
        LocationRequest locationRequest = new LocationRequest(52.2297, 21.0122);

        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0)
        );

        List<DailyForecastDto> forecastDtos = List.of(
                new DailyForecastDto(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 2.19, 1008.0)
        );

        when(openMeteoService.getWeatherForecast(52.2297, 21.0122)).thenReturn(weatherData);
        when(weatherMappingService.mapToForecastDtos(weatherData)).thenReturn(forecastDtos);

        WeatherForecastResponse result = weatherService.getWeatherForecast(locationRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.forecast()).hasSize(1);
        Assertions.assertThat(result.forecast().get(0).date()).isEqualTo(LocalDate.of(2025, 6, 17));
        Assertions.assertThat(result.forecast().get(0).weatherCode()).isEqualTo(3);

        verify(openMeteoService).getWeatherForecast(52.2297, 21.0122);
        verify(weatherMappingService).mapToForecastDtos(weatherData);
    }

    @Test
    void shouldGetWeatherSummary() {
        LocationRequest locationRequest = new LocationRequest(52.2297, 21.0122);

        List<DailyWeatherData> weatherData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0),
                new DailyWeatherData(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 10.63, 1005.6)
        );

        WeatherSummaryResponse expectedResponse = new WeatherSummaryResponse(
                1006.8, 9.7, 14.3, 25.8, "bez opadów"
        );

        when(openMeteoService.getWeatherForecast(52.2297, 21.0122)).thenReturn(weatherData);
        when(weatherMappingService.mapToSummaryResponse(weatherData, weatherSummaryService))
                .thenReturn(expectedResponse);

        WeatherSummaryResponse result = weatherService.getWeatherSummary(locationRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.averagePressure()).isEqualTo(1006.8);
        Assertions.assertThat(result.averageSunshineHours()).isEqualTo(9.7);
        Assertions.assertThat(result.minTemperature()).isEqualTo(14.3);
        Assertions.assertThat(result.maxTemperature()).isEqualTo(25.8);
        Assertions.assertThat(result.weekSummary()).isEqualTo("bez opadów");

        verify(openMeteoService).getWeatherForecast(52.2297, 21.0122);
        verify(weatherMappingService).mapToSummaryResponse(weatherData, weatherSummaryService);
    }

    @Test
    void shouldThrowValidationExceptionWhenLatitudeIsNull() {
        LocationRequest locationRequest = new LocationRequest(null, 21.0122);

        Assertions.assertThatThrownBy(() -> weatherService.getWeatherForecast(locationRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude and longitude are required");

        verifyNoInteractions(openMeteoService, weatherMappingService);
    }

    @Test
    void shouldThrowValidationExceptionWhenLongitudeIsNull() {
        LocationRequest locationRequest = new LocationRequest(52.2297, null);

        Assertions.assertThatThrownBy(() -> weatherService.getWeatherSummary(locationRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude and longitude are required");

        verifyNoInteractions(openMeteoService, weatherMappingService);
    }

    @Test
    void shouldThrowValidationExceptionWhenBothCoordinatesAreNull() {
        LocationRequest locationRequest = new LocationRequest(null, null);

        Assertions.assertThatThrownBy(() -> weatherService.getWeatherForecast(locationRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude and longitude are required");

        verifyNoInteractions(openMeteoService, weatherMappingService);
    }
}
