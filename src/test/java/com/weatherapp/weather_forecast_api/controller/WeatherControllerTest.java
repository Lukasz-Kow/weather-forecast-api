package com.weatherapp.weather_forecast_api.controller;

import com.weatherapp.weather_forecast_api.dto.DailyForecastDto;
import com.weatherapp.weather_forecast_api.dto.LocationRequest;
import com.weatherapp.weather_forecast_api.dto.WeatherForecastResponse;
import com.weatherapp.weather_forecast_api.dto.WeatherSummaryResponse;
import com.weatherapp.weather_forecast_api.exception.DataProcessingException;
import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private WeatherService weatherService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldGetWeatherForecast() throws Exception {
        List<DailyForecastDto> forecastDtos = List.of(
                new DailyForecastDto(LocalDate.of(2025, 6, 17), 3, 14.3, 22.8, 2.19, 1008.0),
                new DailyForecastDto(LocalDate.of(2025, 6, 18), 51, 16.7, 25.8, 2.95, 1005.6)
        );

        WeatherForecastResponse response = new WeatherForecastResponse(forecastDtos);


        when(weatherService.getWeatherForecast(any(LocationRequest.class))).thenReturn(response);

        mvc.perform(post("/api/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecast[0].date").value("17/06/2025"))
                .andExpect(jsonPath("$.forecast[0].weatherCode").value(3))
                .andExpect(jsonPath("$.forecast[0].minTemperature").value(14.3))
                .andExpect(jsonPath("$.forecast[0].maxTemperature").value(22.8))
                .andExpect(jsonPath("$.forecast[0].energyGenerated").value(2.19))
                .andExpect(jsonPath("$.forecast[0].pressure").value(1008.0));
    }

    @Test
    void shouldGetWeatherSummary() throws Exception {
        WeatherSummaryResponse response = new WeatherSummaryResponse(
                1007.26, 8.5, 12.1, 29.0, "bez opadów"
        );

        when(weatherService.getWeatherSummary(any(LocationRequest.class))).thenReturn(response);

        mvc.perform(post("/api/weather/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePressure").value(1007.26))
                .andExpect(jsonPath("$.averageSunshineHours").value(8.5))
                .andExpect(jsonPath("$.minTemperature").value(12.1))
                .andExpect(jsonPath("$.maxTemperature").value(29.0))
                .andExpect(jsonPath("$.weekSummary").value("bez opadów"));
    }

    @Test
    void shouldReturnBadRequestWhenLatitudeIsNull() throws Exception {
        mvc.perform(post("/api/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"longitude\":21.0122}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.errors.latitude").value("Latitude is required."));
    }

    @Test
    void shouldReturnBadRequestWhenLongitudeIsNull() throws Exception {
        mvc.perform(post("/api/weather/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.errors.longitude").value("Longitude is required."));
    }

    @Test
    void shouldReturnBadRequestWhenLatitudeOutOfRange() throws Exception {
        mvc.perform(post("/api/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":95.0,\"longitude\":21.0122}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.errors.latitude").value("Latitude must be between -90.0 and 90.0."));
    }

    @Test
    void shouldReturnBadRequestWhenLongitudeOutOfRange() throws Exception {
        mvc.perform(post("/api/weather/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":185.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.errors.longitude").value("Longitude must be between -180.0 and 180.0."));
    }

    @Test
    void shouldReturnBadRequestWhenValidationExceptionThrown() throws Exception {
        doThrow(new ValidationException("Invalid coordinates")).when(weatherService)
                .getWeatherForecast(any(LocationRequest.class));

        mvc.perform(post("/api/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid coordinates"));
    }

    @Test
    void shouldReturnServiceUnavailableWhenWeatherApiExceptionThrown() throws Exception {
        doThrow(new WeatherApiException("API service unavailable")).when(weatherService)
                .getWeatherSummary(any(LocationRequest.class));

        mvc.perform(post("/api/weather/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Weather API Error"))
                .andExpect(jsonPath("$.message").value("API service unavailable"));
    }

    @Test
    void shouldReturnBadRequestWhenDataProcessingExceptionThrown() throws Exception {
        doThrow(new DataProcessingException("Failed to process data")).when(weatherService)
                .getWeatherForecast(any(LocationRequest.class));

        mvc.perform(post("/api/weather/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Failed to process data"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenGenericExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(weatherService)
                .getWeatherSummary(any(LocationRequest.class));

        mvc.perform(post("/api/weather/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\":52.2297,\"longitude\":21.0122}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
    }
}
