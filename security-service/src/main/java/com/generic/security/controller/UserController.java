package com.generic.security.controller;

import com.generic.security.model.UserDTO;
import com.generic.security.service.SessionService;
import com.generic.security.service.UserService;
import com.generic.security.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {

        userService.createUser(userDTO);
        return new ResponseEntity<>("Successfully Created",HttpStatus.CREATED);
    }

    @GetMapping("/admin/ping")
    public ResponseEntity<String> testSecurity(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @PostMapping("/secure/logout")
    public ResponseEntity<String> secureLogout(HttpServletRequest request,
                                               HttpServletResponse response) {
        LOGGER.info("Logout called");
        request.getSession(false).invalidate();
        String sessionId = CookieUtils.deleteCookies(request, response);
        if(!StringUtils.isEmpty(sessionId))
            sessionService.deleteSession(sessionId);
        return new ResponseEntity<>("Logout Success", HttpStatus.OK);
    }

}
