package com.weather.service;

import com.weather.model.WeatherData;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class WeatherDataService {
    private final ConcurrentMap<String, WeatherData> weatherMap = new ConcurrentHashMap<>();

    public void updateWeatherData(WeatherData newData) {
        weatherMap.put(newData.getCity(), newData);
    }

    public WeatherData getLatestData(String city) {
        return weatherMap.get(city);
    }

    public Collection<WeatherData> getAllData() {
        return weatherMap.values();
    }
}