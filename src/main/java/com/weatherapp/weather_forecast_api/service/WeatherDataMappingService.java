package com.weatherapp.weather_forecast_api.service;

import com.weatherapp.weather_forecast_api.exception.DataProcessingException;
import com.weatherapp.weather_forecast_api.model.DailyWeatherData;
import com.weatherapp.weather_forecast_api.model.OpenMeteo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherDataMappingService {

    public List<DailyWeatherData> mapToWeatherData(OpenMeteo response) {
        List<DailyWeatherData> weatherDataList = new ArrayList<>();
        OpenMeteo.Daily daily = response.getDaily();
        int dataSize = daily.getTime().size();

        for (int i = 0; i < dataSize; i++) {
            try {
                DailyWeatherData data = createDailyWeatherData(daily, i);
                weatherDataList.add(data);
            } catch (Exception e) {

            }
        }

        if (weatherDataList.isEmpty()) {
            throw new DataProcessingException("No valid weather data could be processed");
        }

        return weatherDataList;
    }

    private DailyWeatherData createDailyWeatherData(OpenMeteo.Daily daily, int index) {
        DailyWeatherData data = new DailyWeatherData();

        data.setDate(LocalDate.parse(daily.getTime().get(index), DateTimeFormatter.ISO_LOCAL_DATE));
        data.setWeatherCode(getValueOrDefault(daily.getWeatherCode(), index, 0));
        data.setMinTemperature(getValueOrDefault(daily.getTemperatureMin(), index, 0.0));
        data.setMaxTemperature(getValueOrDefault(daily.getTemperatureMax(), index, 0.0));
        data.setSunshineDuration(getValueOrDefault(daily.getSunshineDuration(), index, 0.0) / 3600.0);
        data.setPressure(getValueOrDefault(daily.getSurfacePressureMean(), index, 1013.25));

        return data;
    }

    private <T> T getValueOrDefault(List<T> list, int index, T defaultValue) {
        return (list != null && index < list.size() && list.get(index) != null)
                ? list.get(index)
                : defaultValue;
    }
}
