package com.odc19.booking;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.odc19.booking",
                "com.odc19.amqp"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.odc19.clients"
)
public class BookingApplication {
    public final static String MODEL_PACKAGE = "com.odc19.booking.entity";
    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
