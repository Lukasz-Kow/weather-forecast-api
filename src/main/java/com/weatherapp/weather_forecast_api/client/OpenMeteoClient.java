package com.weatherapp.weather_forecast_api.client;


import com.weatherapp.weather_forecast_api.config.ApiConfig;
import com.weatherapp.weather_forecast_api.exception.WeatherApiException;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenMeteoClient {

    private final ApiConfig apiConfig;
    private final WebClient.Builder webClientBuilder;

    public OpenMeteo fetchWeatherForecast(Double latitude, Double longitude) {
        try {
            log.info("Fetching weather forecast for lat: {}, lon: {}", latitude, longitude);

            WebClient webClient = webClientBuilder
                    .baseUrl(apiConfig.getBaseUrl()).build();

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(apiConfig.getForecastPath())
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("daily", apiConfig.getDailyParams())
                            .queryParam("timezone", apiConfig.getTimezone())
                            .queryParam("forecast_days", apiConfig.getForecastDays())
                            .build())
                    .retrieve()
                    .bodyToMono(OpenMeteo.class)
                    .block();

        } catch (Exception e) {
            log.error("Error calling Open-Meteo API: {}", e.getMessage(), e);
            throw new WeatherApiException("Failed to fetch weather data: " + e.getMessage());
        }
    }
}
