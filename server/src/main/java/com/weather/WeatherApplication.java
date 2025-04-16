package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }
}

/*
    SpringApplication.run(appname.class, args):
        - starts the Spring application context and initializes the application.
        - automatically detects Configuration classes and registers them as beans in the application context.
        - Starts the embedded server
        - appname.class: specifies the main class to use for the application.
        - args: command-line arguments passed to the application.
*/
