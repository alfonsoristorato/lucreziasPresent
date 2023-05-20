package com.alfonsoristorato.lucreziaspresentbackend.service;

import com.alfonsoristorato.lucreziaspresentbackend.exception.UserException;
import com.alfonsoristorato.lucreziaspresentbackend.model.*;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final UserDetailsService myUserDetails;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsService myUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserDetails = myUserDetails;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .map(user -> {
                    if (myUserDetails.loadUserByUsername(username).isAccountNonLocked()) {
                        if (passwordEncoder.matches(password, user.getPassword())) {
                            user.setAttempts(0);
                            userRepository.save(user);
                            return user;
                        } else {
                            user.setAttempts(user.getAttempts() + 1);
                            userRepository.save(user);
                            throw new UserException(UserError.USER_ERROR("Credenziali non riconosciute."));
                        }
                    } else {
                        throw new UserException(UserError.USER_ERROR("Account bloccato, contatta l'amministratore per sbloccarlo."));
                    }
                })
                .orElseThrow(() -> new UserException(UserError.USER_ERROR("Credenziali non riconosciute.")));
    }

    public String changePassword(PasswordChangeRequestDTO passwordChangeRequestDTO, Principal principal) {
        if (!passwordChangeRequestDTO.getUsername().equals(principal.getName())) {
            throw new UserException(UserError.DISALLOWED_CHANGE("You cannot change the password for another user."));
        }
        User user = validUsernameAndPassword(passwordChangeRequestDTO.getUsername(),
                passwordChangeRequestDTO.getPassword());

        String newPasswordStrength = passwordStrenght(passwordChangeRequestDTO.getNewPassword());
        if (passwordEncoder.matches(passwordChangeRequestDTO.getNewPassword(), user.getPassword())) {
            throw new UserException(UserError.USER_ERROR("La nuova password deve essere diversa dalla vecchia."));
        }
        if (newPasswordStrength.equals("Strong")) {
            user.setPassword(passwordEncoder.encode(passwordChangeRequestDTO.getNewPassword()));
            user.setFirstLogin(false);
            userRepository.save(user);
            return "Password Cambiata.";
        } else {
            String exceptionMessage = String.format("La nuova password ha una sicurezza di tipo: %s. Riprova e assicurati che sia più sicura.", newPasswordStrength);
            throw new UserException(UserError.USER_ERROR(exceptionMessage));
        }
    }

    public String resetUserPassword(Integer userId) {
        String randomPassword = generateRandomFirstPassword();
        return userRepository.findById((long) userId)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(randomPassword));
                    user.setFirstLogin(true);
                    userRepository.save(user);
                    return randomPassword;
                })
                .orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND()));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList());
    }

    public String editUserRole(Integer userId, ChangeUserRoleDTO changeUserRoleDTO) {
        return userRepository.findById((long) userId)
                .map(user -> {
                    user.setRole(changeUserRoleDTO.newRole());
                    userRepository.save(user);
                    return "User role updated.";
                })
                .orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND()));
    }

    public String editUserAttempts(Integer userId, ChangeUserAttemptsDTO changeUserAttemptsDTO) {
        return userRepository.findById((long) userId)
                .map(user -> {
                    user.setAttempts(changeUserAttemptsDTO.newAttempts());
                    userRepository.save(user);
                    return "User attempts updated.";
                })
                .orElseThrow(() -> new UserException(UserError.USER_NOT_FOUND()));
    }

    public String addUser(NewUserRequestDTO newUserRequestDTO) {
        userRepository.findByUsername(newUserRequestDTO.username())
                .ifPresent(user -> {
                    throw new UserException(UserError.USER_ERROR("User already exists."));
                });

        String randomPassword = generateRandomFirstPassword();
        User newUser = User
                .builder()
                .username(newUserRequestDTO.username())
                .password(passwordEncoder.encode(randomPassword))
                .attempts(0)
                .role("utente")
                .firstLogin(true)
                .build();
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
