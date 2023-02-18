package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserAttemptsDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserRoleDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.NewUserRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
                                               @RequestBody ChangeUserRoleDTO changeUserRoleDTO) {
        return new ResponseEntity<>(userService.editUserRole(userId, changeUserRoleDTO), HttpStatus.OK);
    }

    @PatchMapping("/attempts/{userId}")
    public ResponseEntity<String> editUserAttempts(@PathVariable Integer userId,
                                                   @RequestBody ChangeUserAttemptsDTO changeUserAttemptsDTO) {
        return new ResponseEntity<>(userService.editUserAttempts(userId, changeUserAttemptsDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addUser(
            @RequestBody NewUserRequestDTO newUserRequestDTO) {
        return new ResponseEntity<>(userService.addUser(newUserRequestDTO), HttpStatus.OK);
    }

    @PatchMapping("/reset-password/{userId}")
    public ResponseEntity<String> resetUserPassword(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.resetUserPassword(userId), HttpStatus.OK);
    }
}
