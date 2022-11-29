package com.odc19.apigw;

import com.odc19.apigw.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@EnableEurekaClient
@SpringBootApplication
public class ApiGWApplication {
    private final static String clientBaseUrl = "http://localhost:3000/";

    public static void main(String[] args) {
        SpringApplication.run(ApiGWApplication.class, args);
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        corsConfig.setAllowedOriginPatterns(Collections.singletonList(clientBaseUrl));
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setExposedHeaders(Collections.singletonList("Bearer "));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

    /*
     * Inject filter
     * */
    @Autowired
    private JwtAuthenticationFilter filter;

    /*
     *   Must have to use java config instead of application.yml for working
     * */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("rating", r -> r.path("/api/v1/rating/**").uri("lb://rating"))
                .route("user-save", r -> r.path("/api/v1/user/new/shipper/**").uri("lb://user"))
                .route("user-login", r -> r.path("/api/v1/user/login/**").uri("lb://user"))
                .route("booking", r -> r.path("/api/v1/booking/**").uri("lb://booking"))
                .route("user-id", r -> r.path("/api/v1/user/{id}").uri("lb://user"))
                .route("user-forgot-password", r -> r.path("/api/v1/user/password/reset").uri("lb://user"))
                .route("user-shipper", r -> r.path("/api/v1/user/list/shipper").uri("lb://user"))
                .route("user", r -> r.path("/api/v1/user/**").filters(f -> f.filter(filter)).uri("lb://user"))
/*                .route("medical-shop-list-postcode", r -> r.path("/api/v1/medical-shop/list/{postCode}").uri("lb://medical-shop"))
                .route("medical-shop-list", r -> r.path("/api/v1/medical-shop/list").uri("lb://medical-shop"))
                .route("medical-shop-id", r -> r.path("/api/v1/medical-shop/{medicalShopId}").uri("lb://medical-shop"))*/
                .route("medical-shop", r -> r.path("/api/v1/medical-shop/nearest").uri("lb://medical-shop"))
                .route("medical-shop", r -> r.path("/api/v1/medical-shop/new").filters(f -> f.filter(filter)).uri("lb://medical-shop"))
                .route("medical-shop", r -> r.path("/api/v1/medical-shop/goods/new").filters(f -> f.filter(filter)).uri("lb://medical-shop"))
                .route("medical-shop-no-filter", r -> r.path("/api/v1/medical-shop/**").uri("lb://medical-shop"))
                .route("location", r -> r.path("/api/v1/location/**").filters(f -> f.filter(filter)).uri("lb://location"))
                .route("notification", r -> r.path("/api/v1/notification/**").filters(f -> f.filter(filter)).uri("lb://notification"))
                .route("billing", r -> r.path("/api/v1/bill/**").filters(f -> f.filter(filter)).uri("lb://billing"))
                .route("realtime", r -> r.path("/api-sessions/**").filters(f -> f.filter(filter)).uri("lb://realtime"))
                .build();
    }
}
