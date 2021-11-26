package com.airsoftware.secretsantaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SecretSantaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecretSantaApiApplication.class, args);
    }

}
