package com.odc19.otp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.odc19.clients"
)
public class OtpApplication {
    public final static String MODEL_PACKAGE = "com.odc19.otp.entity";

    public static void main(String[] args) {
        SpringApplication.run(OtpApplication.class, args);
    }
}
