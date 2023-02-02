package com.alfonsoristorato.lucreziaspresentbackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;

@Controller
@RequestMapping(path = "/user")
@PreAuthorize("hasAuthority('admin') && principal.user.firstLogin == false")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PatchMapping("/role/{userId}")
    public ResponseEntity<String> editUserRole(@PathVariable Integer userId,
            @RequestBody Map<String, String> requestBody)
            throws Exception {
        return new ResponseEntity<>(userService.editUserRole(userId, requestBody.get("newRole")), HttpStatus.OK);
    }

    @PatchMapping("/attempts/{userId}")
    public ResponseEntity<String> editUserAttempts(@PathVariable Integer userId,
            @RequestBody Map<String, Integer> requestBody)
            throws Exception {
        return new ResponseEntity<>(userService.editUserAttempts(userId, requestBody.get("newAttempts")),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addUser(
            @RequestBody Map<String, String> requestBody)
            throws Exception {
        return new ResponseEntity<>(userService.addUser(requestBody), HttpStatus.OK);
    }

    @PatchMapping("/password/{userId}")
    public ResponseEntity<String> resetUserPassword(@PathVariable Integer userId)
            throws Exception {
        return new ResponseEntity<>(userService.resetUserPassword(userId), HttpStatus.OK);
    }
}
