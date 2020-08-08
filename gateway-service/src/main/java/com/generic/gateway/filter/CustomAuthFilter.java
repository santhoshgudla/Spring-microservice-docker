package com.generic.gateway.filter;

import com.generic.gateway.service.AuthenticationFactory;
import com.generic.gateway.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthFilter.class);

    @Autowired
    private AuthenticationFactory authenticationFactory;

    public CustomAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            LOGGER.info("Got Request to filter");
            ServerHttpRequest request = exchange.getRequest();
            AuthenticationService authenticationService =
                    authenticationFactory.getAuthenticationService();
            if(authenticationService != null){
                boolean isValid =authenticationService.isValidAccess(request);
                if (isValid) {
                    return chain.filter(exchange);
                }
            }
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        });
    }

    static class Config {
    }
}
