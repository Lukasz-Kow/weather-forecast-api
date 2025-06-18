package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.client.OpenMeteoClient;
import com.weatherapp.weather_forecast_api.exception.DataProcessingException;
import com.weatherapp.weather_forecast_api.exception.ValidationException;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenMeteoService {
    private final OpenMeteoClient openMeteoClient;
    private final WeatherValidationService validationService;
    private final WeatherDataMappingService dataMappingService;

    @Autowired
    public OpenMeteoService(OpenMeteoClient openMeteoClient,
                            WeatherValidationService validationService,
                            WeatherDataMappingService dataMappingService) {
        this.openMeteoClient = openMeteoClient;
        this.validationService = validationService;
        this.dataMappingService = dataMappingService;
    }

    public List<DailyWeatherData> getWeatherForecast(Double latitude, Double longitude) {
        validationService.validateCoordinates(latitude, longitude);

        try {
            OpenMeteo response = openMeteoClient.fetchWeatherForecast(latitude, longitude);
            validationService.validateResponse(response);

            return dataMappingService.mapToWeatherData(response);

        } catch (WeatherApiException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataProcessingException("Failed to process weather data: " + e.getMessage(), e);
        }
    }
}
