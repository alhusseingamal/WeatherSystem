package com.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.net.ServerSocket;

@Configuration
public class TcpServerConfig {
    private static final int TCP_PORT = 12345;

    // The @Bean-annotated serverSocket() method is called once during application startup.
    // By default, Spring creates a singleton bean (single instance shared across the app).
    @Bean
    public ServerSocket serverSocket() throws IOException {
        return new ServerSocket(TCP_PORT);
    }
}