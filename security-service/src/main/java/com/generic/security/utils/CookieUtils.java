package com.generic.security.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CookieUtils {

    public static void createCookie(String domain, String accessToken, HttpServletResponse response){
        Cookie cookie = new Cookie("access-token", accessToken);
//        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public static String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            Optional<Cookie> first = Arrays.stream(cookies).filter(c -> c.getName()
                    .equalsIgnoreCase("access-token")).findFirst();
            if (first.isPresent()) {
                return first.get().getValue();
            }
        }
        return null;
    }

    public static String deleteCookies(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        AtomicReference<String> sessionId = null;
        if(cookies != null && cookies.length > 0) {
            Arrays.stream(cookies).forEach(c -> {
                if(c.getName().equalsIgnoreCase("access-token")) {
                    sessionId.set(c.getValue());
                }
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                c.setDomain("localhost");
                response.addCookie(c);
            });
        }
        return sessionId.get();
    }
}
