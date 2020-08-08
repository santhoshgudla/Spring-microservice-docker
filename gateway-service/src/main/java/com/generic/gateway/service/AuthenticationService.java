package com.generic.gateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface AuthenticationService {

    boolean isValidAccess(ServerHttpRequest request);
}
