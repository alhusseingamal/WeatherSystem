package com.weather.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientRunner {
    private static final int THREAD_COUNT = 5;
    private static final int REQUESTS_PER_THREAD = 10;
    private static final AtomicInteger successCount = new AtomicInteger(0);
    private static final String[] TEST_CITIES = {"London", "Paris", "Tokyo", "New York", "Berlin"};

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting stress test with " + THREAD_COUNT + " threads...");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    String city = TEST_CITIES[threadId % TEST_CITIES.length];
                    if (makeRequest(city)) {
                        successCount.incrementAndGet();
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        System.out.println("\nStress test completed");
        System.out.println("Successful requests: " + successCount.get() + "/" + (THREAD_COUNT * REQUESTS_PER_THREAD));
    }

    private static boolean makeRequest(String city) {
        try {
            WeatherClient.fetchWeatherData(city);
            return true;
        } catch (Exception e) {
            System.err.println("Request failed for " + city + ": " + e.getMessage());
            return false;
        }
    }
}