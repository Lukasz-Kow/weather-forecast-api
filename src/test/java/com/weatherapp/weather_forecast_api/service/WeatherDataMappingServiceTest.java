package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.exception.DataProcessingException;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class WeatherDataMappingServiceTest {

    private WeatherDataMappingService weatherDataMappingService;

    @BeforeEach
    void setUp() {
        weatherDataMappingService = new WeatherDataMappingService();
    }

    @Test
    void shouldMapToWeatherDataSuccessfully() {
        OpenMeteo response = createValidOpenMeteoResponse();

        List<DailyWeatherData> result = weatherDataMappingService.mapToWeatherData(response);

        Assertions.assertThat(result).hasSize(2);

        DailyWeatherData firstDay = result.get(0);
        Assertions.assertThat(firstDay.getDate()).isEqualTo(LocalDate.of(2025, 6, 17));
        Assertions.assertThat(firstDay.getWeatherCode()).isEqualTo(3);
        Assertions.assertThat(firstDay.getMinTemperature()).isEqualTo(14.3);
        Assertions.assertThat(firstDay.getMaxTemperature()).isEqualTo(22.8);
        Assertions.assertThat(firstDay.getSunshineDuration()).isCloseTo(8.76, Assertions.within(0.01));
        Assertions.assertThat(firstDay.getPressure()).isEqualTo(1008.0);
    }

    @Test
    void shouldHandleNullValuesWithDefaults() {
        OpenMeteo response = createOpenMeteoResponseWithNulls();

        List<DailyWeatherData> result = weatherDataMappingService.mapToWeatherData(response);

        Assertions.assertThat(result).hasSize(1);

        DailyWeatherData day = result.get(0);
        Assertions.assertThat(day.getWeatherCode()).isEqualTo(0);
        Assertions.assertThat(day.getMinTemperature()).isEqualTo(0.0);
        Assertions.assertThat(day.getMaxTemperature()).isEqualTo(0.0);
        Assertions.assertThat(day.getSunshineDuration()).isEqualTo(0.0);
        Assertions.assertThat(day.getPressure()).isEqualTo(1013.25);
    }

    @Test
    void shouldThrowDataProcessingExceptionWhenNoValidData() {
        OpenMeteo response = createEmptyOpenMeteoResponse();

        Assertions.assertThatThrownBy(() -> weatherDataMappingService.mapToWeatherData(response))
                .isInstanceOf(DataProcessingException.class)
                .hasMessageContaining("No valid weather data could be processed");
    }

    @Test
    void shouldSkipInvalidDataAndContinueProcessing() {
        OpenMeteo response = createOpenMeteoResponseWithInvalidData();

        List<DailyWeatherData> result = weatherDataMappingService.mapToWeatherData(response);

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getDate()).isEqualTo(LocalDate.of(2025, 6, 18));
    }

    @Test
    void shouldHandleExactSunshineConversion() {
        OpenMeteo response = createResponseWithExactSunshineValues();

        List<DailyWeatherData> result = weatherDataMappingService.mapToWeatherData(response);

        Assertions.assertThat(result).hasSize(1);
        DailyWeatherData day = result.get(0);
        Assertions.assertThat(day.getSunshineDuration()).isEqualTo(1.0);
    }

    private OpenMeteo createValidOpenMeteoResponse() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();

        daily.setTime(List.of("2025-06-17", "2025-06-18"));
        daily.setWeatherCode(List.of(3, 51));
        daily.setTemperatureMin(List.of(14.3, 16.7));
        daily.setTemperatureMax(List.of(22.8, 25.8));
        daily.setSunshineDuration(List.of(31549.33, 42575.05));
        daily.setSurfacePressureMean(List.of(1008.0, 1005.6));

        response.setDaily(daily);
        return response;
    }

    private OpenMeteo createOpenMeteoResponseWithNulls() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();

        daily.setTime(List.of("2025-06-17"));
        List<Integer> weatherCodes = new ArrayList<>();
        weatherCodes.add(null);
        daily.setWeatherCode(weatherCodes);

        List<Double> minTemps = new ArrayList<>();
        minTemps.add(null);
        daily.setTemperatureMin(minTemps);

        List<Double> maxTemps = new ArrayList<>();
        maxTemps.add(null);
        daily.setTemperatureMax(maxTemps);

        List<Double> sunshine = new ArrayList<>();
        sunshine.add(null);
        daily.setSunshineDuration(sunshine);

        List<Double> pressure = new ArrayList<>();
        pressure.add(null);
        daily.setSurfacePressureMean(pressure);

        response.setDaily(daily);
        return response;
    }

    private OpenMeteo createEmptyOpenMeteoResponse() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();

        daily.setTime(List.of());
        response.setDaily(daily);
        return response;
    }

    private OpenMeteo createOpenMeteoResponseWithInvalidData() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();

        daily.setTime(List.of("invalid-date", "2025-06-18"));
        daily.setWeatherCode(List.of(3, 51));
        daily.setTemperatureMin(List.of(14.3, 16.7));
        daily.setTemperatureMax(List.of(22.8, 25.8));
        daily.setSunshineDuration(List.of(31549.33, 42575.05));
        daily.setSurfacePressureMean(List.of(1008.0, 1005.6));

        response.setDaily(daily);
        return response;
    }

    private OpenMeteo createResponseWithExactSunshineValues() {
        OpenMeteo response = new OpenMeteo();
        OpenMeteo.Daily daily = new OpenMeteo.Daily();

        daily.setTime(List.of("2025-06-17"));
        daily.setWeatherCode(List.of(1));
        daily.setTemperatureMin(List.of(15.0));
        daily.setTemperatureMax(List.of(25.0));
        daily.setSunshineDuration(List.of(3600.0));
        daily.setSurfacePressureMean(List.of(1010.0));

        response.setDaily(daily);
        return response;
    }
}
