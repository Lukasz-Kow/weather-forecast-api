package com.weatherapp.weather_forecast_api.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolarEnergyServiceTest {

    private SolarEnergyService solarEnergyService;

    @BeforeEach
    void setUp() {
        solarEnergyService = new SolarEnergyService();
    }

    @Test
    void shouldCalculateEnergyProductionCorrectly() {
        double sunshineHours = 8.76;

        double result = solarEnergyService.calculateEnergyProduction(sunshineHours);

        Assertions.assertThat(result).isEqualTo(4.38);
    }

    @Test
    void shouldCalculateEnergyProductionForZeroSunshine() {
        double sunshineHours = 0.0;

        double result = solarEnergyService.calculateEnergyProduction(sunshineHours);

        Assertions.assertThat(result).isEqualTo(0.0);
    }

    @Test
    void shouldCalculateEnergyProductionForMaxSunshine() {
        double sunshineHours = 15.32;

        double result = solarEnergyService.calculateEnergyProduction(sunshineHours);

        Assertions.assertThat(result).isEqualTo(7.66);
    }

    @Test
    void shouldHandleNegativeSunshineHours() {
        double sunshineHours = -5.0;

        double result = solarEnergyService.calculateEnergyProduction(sunshineHours);

        Assertions.assertThat(result).isEqualTo(0.0);
    }

    @Test
    void shouldRoundResultToTwoDecimalPlaces() {
        double sunshineHours = 10.333;

        double result = solarEnergyService.calculateEnergyProduction(sunshineHours);

        Assertions.assertThat(result).isEqualTo(5.17);
    }
}
