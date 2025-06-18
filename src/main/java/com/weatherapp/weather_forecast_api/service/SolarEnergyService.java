package com.weatherapp.weather_forecast_api.service;

import org.springframework.stereotype.Service;

@Service
public class SolarEnergyService {
    private static final double INSTALLATION_POWER_KW = 2.5;
    private static final double PANEL_EFFICIENCY = 0.2;

    public double calculateEnergyProduction(double sunshineHours) {
        if (sunshineHours < 0) {
            sunshineHours = 0;
        }

        double energyProduced = INSTALLATION_POWER_KW * sunshineHours * PANEL_EFFICIENCY;
        return Math.round(energyProduced * 100.0) / 100.0;
    }
}
