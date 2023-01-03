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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.validUsernameAndPassword(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (userOptional.isPresent()) {
            return new ResponseEntity<>("Logged in", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
