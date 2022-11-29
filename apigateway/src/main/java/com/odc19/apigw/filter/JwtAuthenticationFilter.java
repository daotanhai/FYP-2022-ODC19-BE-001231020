package com.odc19.apigw.filter;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        get token
        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        String[] parts = authHeader.split(" ");
//        token start with "Bearer "
        String token = parts[0] + " " + parts[1];
//        attached token into header
        exchange.getRequest().mutate().header(AUTHORIZATION, token).build();
        return chain.filter(exchange);
    }
}
