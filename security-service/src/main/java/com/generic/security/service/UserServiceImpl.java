package com.generic.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.security.domain.User;
import com.generic.security.model.UserDTO;
import com.generic.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDTO userDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.convertValue(userDTO, User.class);
            user.setUuId(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);
        } catch (Exception e){
            LOGGER.error("Unable to save user: ", e);
        }
    }
}
