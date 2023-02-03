package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.model.PasswordChangeRequest;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService myUserDetails;

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

    public String changePassword(PasswordChangeRequest passwordChangeRequest) throws Exception {
        Optional<User> user = validUsernameAndPassword(passwordChangeRequest.getUsername(),
                passwordChangeRequest.getPassword());
        if (user.isPresent()) {
            String newPasswordStrenght = passwordStrenght(passwordChangeRequest.getNewPassword());
            if (passwordEncoder.matches(passwordChangeRequest.getNewPassword(), user.get().getPassword())) {
                throw new Exception("La nuova password deve essere diversa dalla vecchia.");
            }
            if (newPasswordStrenght.equals("Strong")) {
                user.get().setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
                user.get().setFirstLogin(false);
                userRepository.save(user.get());
                return "Password Cambiata.";
            } else {
                String excpetionMessage = "La nuova password ha una sicurezza di tipo: " + newPasswordStrenght
                        + ". Riprova e assicurati che sia più sicura.";
                throw new Exception(excpetionMessage);
            }
        } else {
            throw new Exception("No such user found.");
        }
    }

    public String resetUserPassword(Integer userId) throws Exception {
        String randomPassword = generateRandomFirstPassword();
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(randomPassword));
            user.get().setFirstLogin(true);
            userRepository.save(user.get());
            return randomPassword;
        }
        throw new Exception("User not found");
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
        String randomPassword = generateRandomFirstPassword();
        User newUser = new User();
        newUser.setUsername(requestBody.get("username"));
        newUser.setPassword(passwordEncoder.encode(randomPassword));
        newUser.setAttempts(0);
        newUser.setRole("utente");
        newUser.setFirstLogin(true);
        userRepository.save(newUser);
        return randomPassword;
    }

    private String passwordStrenght(String newPassword) {
        // Checking lower alphabet in string
        int n = newPassword.length();
        boolean hasLower = false, hasUpper = false,
                hasDigit = false;
        Set<Character> specialCharactersUser = new HashSet<>();
        Set<Character> set = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));
        for (char i : newPassword.toCharArray()) {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                specialCharactersUser.add(i);
        }

        if (hasDigit && hasLower && hasUpper && specialCharactersUser.size() > 1
                && (n >= 8))
            return "Strong";
        else if ((hasLower || hasUpper || specialCharactersUser.size() < 2)
                && (n >= 6))
            return "'Media'";
        else
            return "'Debole'";
    }

    private String generateRandomFirstPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

}
