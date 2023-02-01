package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService myUserDetails;

    @Value("${app.default-password}")
    private String defaultPassword;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @SneakyThrows
    public Optional<User> validUsernameAndPassword(String username, String password) {
        Optional<User> user = getUserByUsername(username);

        if (user.isPresent()) {
            if (myUserDetails.loadUserByUsername(username).isAccountNonLocked()) {
                if (passwordEncoder.matches(password, user.get().getPassword())) {
                    user.get().setAttempts(0);
                    userRepository.save(user.get());
                    return user;
                } else {
                    user.get().setAttempts(user.get().getAttempts() + 1);
                    userRepository.save(user.get());
                }
            } else {
                throw new Exception("Account bloccato, contatta l'amministratore per sbloccarlo.");
            }

        }
        throw new Exception("Credenziali non riconosciute.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String editUserRole(Integer userId, String newRole) throws Exception {
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setRole(newRole);
            userRepository.save(user.get());
            return "User role updated";
        }
        throw new Exception("User not found");
    }

    public String editUserAttempts(Integer userId, Integer newAttempts) throws Exception {
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setAttempts(newAttempts);
            userRepository.save(user.get());
            return "User attempts updated";
        }
        throw new Exception("User not found");
    }

    public String addUser(Map<String, String> requestBody) {
        User newUser = new User();
        newUser.setUsername(requestBody.get("username"));
        newUser.setPassword(passwordEncoder.encode(defaultPassword));
        newUser.setAttempts(0);
        newUser.setRole("utente");
        newUser.setFirstLogin(true);
        userRepository.save(newUser);
        return "User added";
    }
}
