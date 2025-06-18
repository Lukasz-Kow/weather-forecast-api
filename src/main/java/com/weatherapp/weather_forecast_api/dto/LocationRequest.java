package com.weatherapp.weather_forecast_api.dto;

import jakarta.validation.constraints.*;

public record LocationRequest (

    @NotNull(message = "Latitude is required.")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90.0 and 90.0.")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90.0 and 90.0.")
    Double latitude,

    @NotNull(message = "Longitude is required.")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180.0 and 180.0.")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180.0 and 180.0.")
    Double longitude

) {}
