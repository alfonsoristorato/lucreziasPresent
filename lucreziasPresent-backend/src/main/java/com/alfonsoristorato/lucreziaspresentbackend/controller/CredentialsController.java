package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.LoginRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.PasswordChangeRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping
public class CredentialsController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(userService.validUsernameAndPassword(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO, Principal principal) {
        return new ResponseEntity<>(userService.changePassword(passwordChangeRequestDTO, principal),
                HttpStatus.OK);
    }

}
