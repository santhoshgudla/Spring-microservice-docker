package com.generic.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFactory.class);

    @Autowired
    private AuthenticationService basicAuthenticationService;

    @Autowired
    private AuthenticationService keyCloakAuthenticationService;

    @Value("${security.type}")
    private String securityType;

    public AuthenticationService getAuthenticationService(){
        switch (securityType) {
            case "BASIC":
                return basicAuthenticationService;
            case "KEYCLOAK":
                return keyCloakAuthenticationService;
            default:
                LOGGER.info("Authentication type not implemented: "+securityType);
                return null;
        }
    }
}
