package com.weather.sensor;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;

public class WeatherSensor {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String serverIp;
    private static int serverPort_TCP;

    static {
        loadConfig();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Weather Sensor Client");
            System.out.println("Connecting to server at " + serverIp + ":" + serverPort_TCP);

            while (true) {
                System.out.print("\nEnter city name (or 'exit' to quit): ");
                String city = scanner.nextLine().trim();
                
                if (city.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.print("Enter temperature in °C: ");
                String temperature = scanner.nextLine().trim();

                System.out.print("Enter humidity in %: ");
                String humidity = scanner.nextLine().trim();

                sendWeatherData(city, temperature, humidity);
            }
        }
    }

    public static void sendWeatherData(String city, String temperature, String humidity) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String data = String.format("%s|%s|%s|%s", city, temperature, humidity, timestamp);

        try (Socket socket = new Socket(serverIp, serverPort_TCP);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            out.println(data);
            System.out.println("Sent: " + data);
            
            String response = in.readLine();
            System.out.println("Server response: " + response);
            
        } catch (ConnectException e) {
            System.err.println("Connection failed. Is the server running?");
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        }
    }

    private static void loadConfig() {
        try (InputStream input = WeatherSensor.class.getClassLoader()
                .getResourceAsStream("sensor-config.properties")) {
            
            Properties prop = new Properties();
            prop.load(input);
            
            serverIp = prop.getProperty("server.ip", "localhost");
            serverPort_TCP = Integer.parseInt(prop.getProperty("tcp.server.port", "12345"));
            
        } catch (Exception e) {
            System.err.println("Error loading config, using defaults");
            serverIp = "localhost";
            serverPort_TCP = 12345;
        }
    }
}