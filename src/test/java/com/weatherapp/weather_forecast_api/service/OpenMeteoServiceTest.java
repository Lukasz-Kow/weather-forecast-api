package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.client.OpenMeteoClient;
import com.weatherapp.weather_forecast_api.exception.DataProcessingException;
import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
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
class OpenMeteoServiceTest {

    @Mock
    private OpenMeteoClient openMeteoClient;

    @Mock
    private WeatherValidationService validationService;

    @Mock
    private WeatherDataMappingService dataMappingService;

    private OpenMeteoService openMeteoService;

    @BeforeEach
    void setUp() {
        openMeteoService = new OpenMeteoService(
                openMeteoClient,
                validationService,
                dataMappingService
        );
    }

    @Test
    void shouldGetWeatherForecast() {
        Double latitude = 52.2297;
        Double longitude = 21.0122;

        OpenMeteo mockResponse = new OpenMeteo();
        List<DailyWeatherData> expectedData = List.of(
                new DailyWeatherData(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 8.76, 1008.0)
        );

        when(openMeteoClient.fetchWeatherForecast(latitude, longitude)).thenReturn(mockResponse);
        when(dataMappingService.mapToWeatherData(mockResponse)).thenReturn(expectedData);

        List<DailyWeatherData> result = openMeteoService.getWeatherForecast(latitude, longitude);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getDate()).isEqualTo(LocalDate.of(2025, 6, 17));

        verify(validationService).validateCoordinates(latitude, longitude);
        verify(validationService).validateResponse(mockResponse);
        verify(openMeteoClient).fetchWeatherForecast(latitude, longitude);
        verify(dataMappingService).mapToWeatherData(mockResponse);
    }

    @Test
    void shouldThrowValidationExceptionWhenCoordinatesInvalid() {
        Double latitude = 95.0;
        Double longitude = 21.0122;

        doThrow(new ValidationException("Latitude must be between -90.0 and 90.0"))
                .when(validationService).validateCoordinates(latitude, longitude);

        Assertions.assertThatThrownBy(() -> openMeteoService.getWeatherForecast(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");

        verify(validationService).validateCoordinates(latitude, longitude);
        verifyNoInteractions(openMeteoClient, dataMappingService);
    }

    @Test
    void shouldThrowWeatherApiExceptionWhenClientFails() {
        Double latitude = 52.2297;
        Double longitude = 21.0122;

        when(openMeteoClient.fetchWeatherForecast(latitude, longitude))
                .thenThrow(new WeatherApiException("API service unavailable"));

        Assertions.assertThatThrownBy(() -> openMeteoService.getWeatherForecast(latitude, longitude))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("API service unavailable");

        verify(validationService).validateCoordinates(latitude, longitude);
        verify(openMeteoClient).fetchWeatherForecast(latitude, longitude);
        verifyNoInteractions(dataMappingService);
    }

    @Test
    void shouldThrowDataProcessingExceptionWhenUnexpectedErrorOccurs() {
        Double latitude = 52.2297;
        Double longitude = 21.0122;

        OpenMeteo mockResponse = new OpenMeteo();

        when(openMeteoClient.fetchWeatherForecast(latitude, longitude)).thenReturn(mockResponse);
        when(dataMappingService.mapToWeatherData(mockResponse))
                .thenThrow(new RuntimeException("Unexpected mapping error"));

        Assertions.assertThatThrownBy(() -> openMeteoService.getWeatherForecast(latitude, longitude))
                .isInstanceOf(DataProcessingException.class)
                .hasMessageContaining("Failed to process weather data");

        verify(validationService).validateCoordinates(latitude, longitude);
        verify(validationService).validateResponse(mockResponse);
        verify(openMeteoClient).fetchWeatherForecast(latitude, longitude);
        verify(dataMappingService).mapToWeatherData(mockResponse);
    }
}
