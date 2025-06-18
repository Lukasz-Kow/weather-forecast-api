package com.weatherapp.weather_forecast_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteo {
    private Double latitude;
    private Double longitude;

    @JsonProperty("generationtime_ms")
    private Double generationtimeMs;

    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;

    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;

    private Double elevation;

    @JsonProperty("daily_units")
    private DailyUnits dailyUnits;

    private Daily daily;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyUnits {
        private String time;

        @JsonProperty("temperature_2m_max")
        private String temperatureMax;

        @JsonProperty("temperature_2m_min")
        private String temperatureMin;

        @JsonProperty("weather_code")
        private String weatherCode;

        @JsonProperty("sunshine_duration")
        private String sunshineDuration;

        @JsonProperty("surface_pressure_mean")
        private String surfacePressureMean;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Daily {
        private List<String> time;

        @JsonProperty("temperature_2m_max")
        private List<Double> temperatureMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperatureMin;

        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        @JsonProperty("sunshine_duration")
        private List<Double> sunshineDuration;

        @JsonProperty("surface_pressure_mean")
        private List<Double> surfacePressureMean;
    }
}
