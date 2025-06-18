package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class WeatherValidationServiceTest {

    private WeatherValidationService weatherValidationService;

    @BeforeEach
    void setUp() {
        weatherValidationService = new WeatherValidationService();
    }

    @Test
    void shouldValidateValidCoordinates() {
        Double latitude = 52.2297;
        Double longitude = 21.0122;

        Assertions.assertThatCode(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowValidationExceptionWhenLatitudeIsNull() {
        Double latitude = null;
        Double longitude = 21.0122;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude and longitude cannot be null");
    }

    @Test
    void shouldThrowValidationExceptionWhenLongitudeIsNull() {
        Double latitude = 52.2297;
        Double longitude = null;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude and longitude cannot be null");
    }

    @Test
    void shouldThrowValidationExceptionWhenLatitudeTooHigh() {
        Double latitude = 95.0;
        Double longitude = 21.0122;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");
    }

    @Test
    void shouldThrowValidationExceptionWhenLatitudeTooLow() {
        Double latitude = -95.0;
        Double longitude = 21.0122;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");
    }

    @Test
    void shouldThrowValidationExceptionWhenLongitudeTooHigh() {
        Double latitude = 52.2297;
        Double longitude = 185.0;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Longitude must be between -180.0 and 180.0");
    }

    @Test
    void shouldThrowValidationExceptionWhenLongitudeTooLow() {
        Double latitude = 52.2297;
        Double longitude = -185.0;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateCoordinates(latitude, longitude))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Longitude must be between -180.0 and 180.0");
    }

    @Test
    void shouldValidateValidResponse() {
        OpenMeteo response = createValidOpenMeteoResponse();

        Assertions.assertThatCode(() -> weatherValidationService.validateResponse(response))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWeatherApiExceptionWhenResponseIsNull() {
        OpenMeteo response = null;

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateResponse(response))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("No response received from Open-Meteo API");
    }

    @Test
    void shouldThrowWeatherApiExceptionWhenDailyIsNull() {
        OpenMeteo response = new OpenMeteo();
        response.setDaily(null);

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateResponse(response))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("No daily weather data received from Open-Meteo API");
    }

    @Test
    void shouldThrowWeatherApiExceptionWhenTimeIsNull() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();
        daily.setTime(null);
        response.setDaily(daily);

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateResponse(response))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("No time data received from API");
    }

    @Test
    void shouldThrowWeatherApiExceptionWhenTimeIsEmpty() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();
        daily.setTime(List.of());
        response.setDaily(daily);

        Assertions.assertThatThrownBy(() -> weatherValidationService.validateResponse(response))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("No time data received from API");
    }

    @Test
    void shouldValidateBoundaryLatitudeValues() {
        Assertions.assertThatCode(() -> weatherValidationService.validateCoordinates(90.0, 0.0))
                .doesNotThrowAnyException();

        Assertions.assertThatCode(() -> weatherValidationService.validateCoordinates(-90.0, 0.0))
                .doesNotThrowAnyException();

        Assertions.assertThatCode(() -> weatherValidationService.validateCoordinates(0.0, 180.0))
                .doesNotThrowAnyException();

        Assertions.assertThatCode(() -> weatherValidationService.validateCoordinates(0.0, -180.0))
                .doesNotThrowAnyException();
    }

    private OpenMeteo createValidOpenMeteoResponse() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();
        daily.setTime(List.of("2025-06-17", "2025-06-18"));
        response.setDaily(daily);
        return response;
    }
}
