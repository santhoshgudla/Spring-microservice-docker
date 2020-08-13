package com.generic.security.service;

import com.generic.security.domain.Session;

import java.util.Optional;

public interface SessionService {

    void createSession(long userId, String sessionId);

    Optional<Session> getSession(String sessionId);

    void deleteSession(String sessionId);
}
