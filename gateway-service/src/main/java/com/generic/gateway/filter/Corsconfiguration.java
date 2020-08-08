package com.generic.gateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class Corsconfiguration {

    private static final String ALLOWED_METHODS = "GET, PUT, POST, PATCH, DELETE, OPTIONS";
    private static final String MAX_AGE = "3600";
    private static final String ALLOWED_HEADERS = "Content-Type, authorization, x-requested-with, Access-Control-Allow-Origin";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if(CorsUtils.isCorsRequest(request)){
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", "http://localhost:3000");
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Credentials","true");
                headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);
            }
            return chain.filter(ctx);
        };
    }
}
