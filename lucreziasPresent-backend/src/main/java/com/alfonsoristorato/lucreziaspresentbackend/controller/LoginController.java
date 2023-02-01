package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.LoginRequest;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) throws Exception {
        Optional<User> authenticated = userService.validUsernameAndPassword(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (authenticated.isPresent()) {
            return new ResponseEntity<>(authenticated.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(authenticated.get(), HttpStatus.UNAUTHORIZED);
    }
}
