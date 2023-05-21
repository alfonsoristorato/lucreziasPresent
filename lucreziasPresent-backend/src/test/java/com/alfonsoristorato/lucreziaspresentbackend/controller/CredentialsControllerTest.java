package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.LoginRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.PasswordChangeRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialsControllerTest {
    @InjectMocks
    private CredentialsController credentialsController;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @Test
    void loginReturnsAnUserAndOk_and_callsUserService() {
        User user = new User(1L, "user", "password", "admin", 0, false);
        when(userService.validUsernameAndPassword("user", "password")).thenReturn(user);
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user", "password");

        ResponseEntity<User> response = credentialsController.login(loginRequestDTO);

        Assertions.assertThat(response.getBody()).isEqualTo(user);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).validUsernameAndPassword("user", "password");
    }

    @Test
    void changePasswordReturnsAStringAndOk_and_callsUserService() {
        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO("user", "password", "newPassword");
        when(userService.changePassword(passwordChangeRequestDTO, principal)).thenReturn("Password Changed.");

        ResponseEntity<String> response = credentialsController.changePassword(passwordChangeRequestDTO, principal);

        Assertions.assertThat(response.getBody()).isEqualTo("Password Changed.");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).changePassword(passwordChangeRequestDTO, principal);
    }

}

