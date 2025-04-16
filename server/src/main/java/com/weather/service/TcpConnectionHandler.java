package com.weather.service;

import com.weather.model.WeatherData;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// @Service
public class TcpConnectionHandler implements Runnable {
    private final Socket clientSocket;
    private final WeatherDataService weatherDataService;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TcpConnectionHandler(Socket socket, WeatherDataService weatherDataService) {
        this.clientSocket = socket;
        this.weatherDataService = weatherDataService;
    }

    @Override
    public void run() { // started when a new thread is created for each client connection (invoked in TcpServerStarter by .start())
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String sensorData = in.readLine();
            System.out.println("Received from sensor: " + sensorData);
            
            // Parse the incoming data, and update the weather data service
            String[] parts = sensorData.split("\\|");
            if (parts.length == 4) {
                String city = parts[0];
                double temperature = Double.parseDouble(parts[1]);
                double humidity = Double.parseDouble(parts[2]);
                String timestamp = parts[3];
                
                weatherDataService.updateWeatherData(
                    new WeatherData(city, temperature, humidity, timestamp)
                );
                
                out.println("ACK: Data received at " + LocalDateTime.now().format(TIMESTAMP_FORMAT));
            } else {
                out.println("ERROR: Invalid data format");
            }
        } catch (IOException e) {
            System.err.println("Error handling sensor connection: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing sensor data: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}