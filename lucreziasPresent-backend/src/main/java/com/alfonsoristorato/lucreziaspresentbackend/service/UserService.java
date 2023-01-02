package com.alfonsoristorato.lucreziaspresentbackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username));
    }

    public Optional<User> validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}
