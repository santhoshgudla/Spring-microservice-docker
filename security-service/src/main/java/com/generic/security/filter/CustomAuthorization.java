package com.generic.security.filter;

import com.generic.security.domain.Session;
import com.generic.security.domain.User;
import com.generic.security.domain.UserPrincipal;
import com.generic.security.repository.UserRepository;
import com.generic.security.service.SessionService;
import com.generic.security.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CustomAuthorization extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorization.class);

    private UserRepository userRepository;

    private SessionService sessionService;

    public CustomAuthorization(AuthenticationManager authenticationManager, UserRepository userRepository,
                               SessionService sessionService) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Read Access token from the cookie
        String accessToken = CookieUtils.getAccessToken(request);
        LOGGER.info("Access token got from cookie : "+accessToken);
        if (StringUtils.isEmpty(accessToken) ) {
            return;
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(String accessToken) {
        if(StringUtils.isEmpty(accessToken))
            return null;
        Optional<Session> sessionOptional = sessionService.getSession(accessToken);
        if (sessionOptional.isPresent() && sessionOptional.get().getExpiration() > 0) {
            Optional<User> userOptional = userRepository.findById(sessionOptional.get().getUserId());
            if (userOptional.isPresent()) {
                UserPrincipal principal = new UserPrincipal(userOptional.get());
                return new UsernamePasswordAuthenticationToken(principal.getUsername(),
                        null, principal.getAuthorities());
            }
        }
        return null;
    }
}
