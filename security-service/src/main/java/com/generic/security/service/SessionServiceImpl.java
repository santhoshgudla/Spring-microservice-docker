package com.generic.security.service;

import com.generic.security.domain.Session;
import com.generic.security.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Value("${session.idle.timeout:300}")
    private long sessionIdleTimeout;

    @Override
    public void createSession(long userId, String sessionId) {
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setExpiration(sessionIdleTimeout);
        sessionRepository.save(session);
    }

    @Override
    public Optional<Session> getSession(String sessionId) {
       return sessionRepository.findById(sessionId);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
