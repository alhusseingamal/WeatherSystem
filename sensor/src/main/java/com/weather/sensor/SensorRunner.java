package com.weather.sensor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class SensorRunner {
    private static final String[] CITIES = {"London", "Paris", "New York", "Tokyo", "Berlin"};
    private static final Random random = new Random();

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        
        for (String city : CITIES) {
            executor.scheduleAtFixedRate(() -> {
                double temp = 15 + random.nextDouble() * 20; // 15-35°C
                double humidity = 30 + random.nextDouble() * 50; // 30-80%
                
                new WeatherSensor().sendWeatherData(
                    city, 
                    String.format("%.1f", temp),
                    String.format("%.1f", humidity)
                );
            }, 0, 10, TimeUnit.SECONDS); // Send every 10 seconds
        }
    }
}