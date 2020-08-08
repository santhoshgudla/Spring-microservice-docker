package com.generic.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.security.domain.Session;
import com.generic.security.domain.UserPrincipal;
import com.generic.security.model.LoginVO;
import com.generic.security.repository.SessionRepository;
import com.generic.security.repository.UserRepository;
import com.generic.security.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class CustomAuthentication extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthentication.class);

    private AuthenticationManager authenticationManager;

    private SessionRepository sessionRepository;

    private UserRepository userRepository;

    private long sessionIdleTimeout;

    public CustomAuthentication(AuthenticationManager authenticationManager,
                                SessionRepository sessionRepository, UserRepository userRepository,
                                long sessionIdleTimeout){
        this.authenticationManager = authenticationManager;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.sessionIdleTimeout = sessionIdleTimeout;
    }

    /* Trigger when we issue POST request to /login
    We also need to pass in {"username":"dan", "password":"dan123"} in the request body
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Grab credentials and map them to login viewmodel
        LoginVO credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginVO.class);
        } catch (IOException e) {
            LOGGER.error("Exception while getting login request details ", e);
        }

        // Create login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());

        // Authenticate user
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        // Grab principal
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        try {
            request.getSession(false).invalidate();
        } catch (NullPointerException e){
            LOGGER.error("Session does not exist");
        }
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        CookieUtils.createCookie("localhost", sessionId, response);
        saveSession(principal.getUserId(), sessionId);
    }

    private void saveSession(long userId, String sessionId) {
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setExpiration(sessionIdleTimeout);
        sessionRepository.save(session);
        Date date = new Date();
        LOGGER.info("Session created for userId: "+userId+ " Session Id: "+sessionId+" at "+date);
    }
}