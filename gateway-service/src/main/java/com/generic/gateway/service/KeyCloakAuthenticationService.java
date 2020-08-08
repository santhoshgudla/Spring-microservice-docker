package com.generic.gateway.service;

import com.generic.gateway.utils.AES;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class KeyCloakAuthenticationService implements AuthenticationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KeyCloakAuthenticationService.class);

    @Value("${keycloak.service-url}")
    private String keycloakServiceUrl;

    @Value("${aes.secret-key}")
    private String secretKey;

    @Override
    public boolean isValidAccess(ServerHttpRequest request) {
        if(!request.getURI().getPath().equalsIgnoreCase("/kc/")){
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            List<HttpCookie> cookie = cookies.get("OAuth_Token_Request_State");
            if(cookie != null && !cookie.isEmpty()){
                return validateSession(cookie.get(0).getValue());
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean validateSession(String cookie) {
        String encryptedCookie = AES.encrypt(cookie, secretKey);
        try {
            HttpResponse<String> response = Unirest.get(keycloakServiceUrl+"/session/valid")
                    .queryString("cookie", encryptedCookie)
                    .asString();
            if(response.getStatus() == 200 && response.getBody().equalsIgnoreCase("VALID")){
                LOGGER.debug("Valid keycloak session");
                return true;
            }
            LOGGER.debug("Invalid keycloak session");
        } catch (UnirestException e) {
            LOGGER.error("Exception in session validate api call: ", e);
        }
        return false;
    }
}
