package com.generic.security.controller;

import com.generic.security.model.UserDTO;
import com.generic.security.service.UserService;
import com.netflix.loadbalancer.PingUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {

        userService.createUser(userDTO);
        return new ResponseEntity<>("Successfully Created",HttpStatus.CREATED);
    }

    @GetMapping("/admin/securl")
    public ResponseEntity<String> testSecurity(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
