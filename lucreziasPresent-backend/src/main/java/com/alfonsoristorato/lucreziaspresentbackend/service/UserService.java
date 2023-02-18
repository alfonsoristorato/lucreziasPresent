package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.exception.UserException;
import com.alfonsoristorato.lucreziaspresentbackend.model.*;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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


    public User validUsernameAndPassword(String username, String password) {
        Optional<User> user = getUserByUsername(username);

        if (user.isPresent()) {
            if (myUserDetails.loadUserByUsername(username).isAccountNonLocked()) {
                if (passwordEncoder.matches(password, user.get().getPassword())) {
                    user.get().setAttempts(0);
                    userRepository.save(user.get());
                    return user.get();
                } else {
                    user.get().setAttempts(user.get().getAttempts() + 1);
                    userRepository.save(user.get());
                }
            } else {
                throw new UserException(UserError.USER_ERROR("Account bloccato, contatta l'amministratore per sbloccarlo."));
            }

        }
        throw new UserException(UserError.USER_ERROR("Credenziali non riconosciute."));
    }

    public String changePassword(PasswordChangeRequestDTO passwordChangeRequestDTO) {
        User user = validUsernameAndPassword(passwordChangeRequestDTO.getUsername(),
                passwordChangeRequestDTO.getPassword());

        String newPasswordStrenght = passwordStrenght(passwordChangeRequestDTO.getNewPassword());
        if (passwordEncoder.matches(passwordChangeRequestDTO.getNewPassword(), user.getPassword())) {
            throw new UserException(UserError.USER_ERROR("La nuova password deve essere diversa dalla vecchia."));
        }
        if (newPasswordStrenght.equals("Strong")) {
            user.setPassword(passwordEncoder.encode(passwordChangeRequestDTO.getNewPassword()));
            user.setFirstLogin(false);
            userRepository.save(user);
            return "Password Cambiata.";
        } else {
            String excpetionMessage = "La nuova password ha una sicurezza di tipo: " + newPasswordStrenght
                    + ". Riprova e assicurati che sia più sicura.";
            throw new UserException(UserError.USER_ERROR(excpetionMessage));
        }
    }

    public String resetUserPassword(Integer userId) {
        String randomPassword = generateRandomFirstPassword();
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(randomPassword));
            user.get().setFirstLogin(true);
            userRepository.save(user.get());
            return randomPassword;
        }
        throw new UserException(UserError.USER_NOT_FOUND());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().sorted(Comparator.comparing(User::getUsername)).toList();
    }

    public String editUserRole(Integer userId, ChangeUserRoleDTO changeUserRoleDTO) {
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setRole(changeUserRoleDTO.newRole());
            userRepository.save(user.get());
            return "User role updated.";
        }
        throw new UserException(UserError.USER_NOT_FOUND());
    }

    public String editUserAttempts(Integer userId, ChangeUserAttemptsDTO changeUserAttemptsDTO) {
        Optional<User> user = userRepository.findById((long) userId);
        if (user.isPresent()) {
            user.get().setAttempts(changeUserAttemptsDTO.newAttempts());
            userRepository.save(user.get());
            return "User attempts updated.";
        }
        throw new UserException(UserError.USER_NOT_FOUND());
    }

    public String addUser(NewUserRequestDTO newUserRequestDTO) {
        Optional<User> user = userRepository.findByUsername(newUserRequestDTO.username());
        if (user.isPresent()) {
            throw new UserException(UserError.USER_ERROR("User already exists."));
        }
        String randomPassword = generateRandomFirstPassword();
        User newUser = new User();
        newUser.setUsername(newUserRequestDTO.username());
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
        Set<Character> set = new HashSet<>(
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
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}
