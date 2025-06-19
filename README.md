# Weather Forecast API

A simple backend API that provides 7-day weather forecasts and solar energy production estimates. This API fetches weather data from Open-Meteo and calculates how much energy solar panels might generate based on weather conditions.

## What it does

- **7-day forecast endpoint**: Returns daily weather data with temperature, weather codes, and estimated solar energy production
- **Weekly summary endpoint**: Provides average pressure, sunshine hours, temperature extremes, and general weather overview
- **Solar energy calculation**: Uses the formula `energy = 2.5kW × sunshine_hours × 0.2 efficiency`
- **Location-based**: Takes latitude and longitude in request body to get local weather data

## API Endpoints

**POST /api/weather/forecast**
- Request body:
```json
{
    "latitude": 52.2297,
    "longitude": 21.0122
}
```
- Returns 7-day forecast with daily data including date, weather code, min/max temps, and estimated solar energy in kWh

**POST /api/weather/summary**  
- Request body:
```json
{
    "latitude": 52.2297,
    "longitude": 21.0122
}
```
- Returns weekly summary with average pressure, sunshine hours, temperature extremes, and weather overview

## Tech details

- Integrates with Open-Meteo API (https://open-meteo.com) for weather data
- Solar panel specs: 2.5kW installation with 0.2 efficiency
- Input validation for latitude/longitude in request body
- Error handling for external API failures
- Returns data in JSON format

## Solar Energy Calculation

The API calculates estimated solar energy production using:
- **Installation power**: 2.5 kW
- **Panel efficiency**: 0.2 (20%)
- **Sunshine hours**: Retrieved from weather API
- **Formula**: `energy_kWh = 2.5 × sunshine_hours × 0.2`

This gives a rough estimate - real solar forecasting is more complex, but this works for our purposes.

## Response Format

All responses are in JSON format with proper error handling for invalid coordinates or API failures.
