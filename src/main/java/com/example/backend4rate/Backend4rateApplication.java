package com.example.backend4rate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Backend4rateApplication {

    public static void main(String[] args) {
        SpringApplication.run(Backend4rateApplication.class, args);
    }

}
