package com.generic.gateway.service;

import com.generic.gateway.domain.Session;
import com.generic.gateway.domain.User;
import com.generic.gateway.repository.SessionRepository;
import com.generic.gateway.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BasicAuthenticationService  implements  AuthenticationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationService.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${session.idle.timeout:300}")
    private long sessionIdleTimeout;

    @Override
    public boolean isValidAccess(ServerHttpRequest request) {
        LOGGER.info("Basic Authentication started");
        if(!request.getURI().getPath().equalsIgnoreCase("/sec/login")){
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            List<HttpCookie> cookie = cookies.get("access-token");
            if(cookie != null && !cookie.isEmpty() && !StringUtils.isEmpty(cookie.get(0).getValue())){
                String accessToken = cookie.get(0).getValue();
                Optional<Session> sessionOptional = sessionRepository.findById(accessToken);
                if (sessionOptional.isPresent() && sessionOptional.get().getExpiration() > 0) {
                    Optional<User> userOptional = userRepository.findById(sessionOptional.get().getUserId());
                    if (userOptional.isPresent()) {
                        sessionOptional.get().setExpiration(sessionIdleTimeout);
                        sessionRepository.save(sessionOptional.get());
                        return true;
                    }
                } else {
                    LOGGER.info("Session expired: "+accessToken);
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
