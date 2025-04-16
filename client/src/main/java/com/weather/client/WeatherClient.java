package com.weather.client;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.Scanner;

public class WeatherClient {
    private static final String CONFIG_FILE = "client-config.properties";
    private static final String DEFAULT_BASE_URL = "http://localhost:8080/api/weather";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static String baseUrl;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = WeatherClient.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                baseUrl = prop.getProperty("server.base.url", DEFAULT_BASE_URL);
            } else {
                baseUrl = DEFAULT_BASE_URL;
                System.err.println("Config file not found, using default URL");
            }
        } catch (Exception e) {
            baseUrl = DEFAULT_BASE_URL;
            System.err.println("Error loading config: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Weather Data Client [Server: " + baseUrl + "]");
        System.out.println("-------------------");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nEnter city name (or 'exit' to quit): ");
                String city = scanner.nextLine().trim();

                if (city.equalsIgnoreCase("exit")) {
                    break;
                }

                fetchWeatherData(city);
            }
        }
    }

    public static void fetchWeatherData(String city) {
        try {
            String encodedCity = city.replace(" ", "%20");
            URI uri = URI.create(baseUrl + "/" + encodedCity);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(java.time.Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\nWeather Data for " + city + ":");
            System.out.println(response.body());
            
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }
}