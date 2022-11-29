package com.odc19.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RealtimeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealtimeApplication.class, args);
    }
}
