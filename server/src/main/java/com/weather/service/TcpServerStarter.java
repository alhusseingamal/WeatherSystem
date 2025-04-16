package com.weather.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpServerStarter {
    private final ServerSocket serverSocket;
    private final WeatherDataService weatherDataService;

    @Autowired
    public TcpServerStarter(ServerSocket serverSocket, // ServerSocket bean created in TcpServerConfig is injected here
                          WeatherDataService weatherDataService) {
        this.serverSocket = serverSocket;
        this.weatherDataService = weatherDataService;
    }

    @PostConstruct
    public void start() { // this method is called after the bean is constructed and dependencies are injected
        new Thread(this::runServer).start(); // a new thread asynchronously invokes the runServer method, TCP server runs independently of the main thread
        System.out.println("TCP Server is starting...");
    }

    private void runServer() {
        System.out.println("TCP Server started on port " + serverSocket.getLocalPort());
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // accept incoming client connections
                new Thread(new TcpConnectionHandler(
                    clientSocket, this.weatherDataService)).start(); // create a new thread to handle each client connection
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
            } catch (IOException e) {
                System.err.println("Server accept error: " + e.getMessage());
                break;
            }
        }
    }
}