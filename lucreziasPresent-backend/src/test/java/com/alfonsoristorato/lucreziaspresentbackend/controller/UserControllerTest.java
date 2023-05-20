package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserAttemptsDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserRoleDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.NewUserRequestDTO;
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

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void getAllUsersReturnsListOfUsersAndOk_and_callsUserService() {
        List<User> userList = List.of(new User(1L, "user", "password", "admin", 0, false));
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        Assertions.assertThat(response.getBody()).isEqualTo(userList);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).getAllUsers();
    }

    @Test
    void editUserRoleReturnsAStringAndOk_and_callsUserService() {
        ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO("user");
        when(userService.editUserRole(1, changeUserRoleDTO)).thenReturn("User role updated.");

        ResponseEntity<String> response = userController.editUserRole(1, changeUserRoleDTO);

        Assertions.assertThat(response.getBody()).isEqualTo("User role updated.");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).editUserRole(1, changeUserRoleDTO);
    }

    @Test
    void editUserAttemptsReturnsAStringAndOk_and_callsUserService() {
        ChangeUserAttemptsDTO changeUserAttemptsDTO = new ChangeUserAttemptsDTO(0);
        when(userService.editUserAttempts(1, changeUserAttemptsDTO)).thenReturn("User attempts updated.");

        ResponseEntity<String> response = userController.editUserAttempts(1, changeUserAttemptsDTO);

        Assertions.assertThat(response.getBody()).isEqualTo("User attempts updated.");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).editUserAttempts(1, changeUserAttemptsDTO);
    }

    @Test
    void addUserReturnsAStringAndOk_and_callsUserService() {
        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO("newUserName");
        when(userService.addUser(newUserRequestDTO)).thenReturn("userPassword");

        ResponseEntity<String> response = userController.addUser(newUserRequestDTO);

        Assertions.assertThat(response.getBody()).isEqualTo("userPassword");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).addUser(newUserRequestDTO);
    }

    @Test
    void resetUserPasswordReturnsAStringAndOk_and_callsUserService() {
        when(userService.resetUserPassword(1)).thenReturn("userNewPassword");

        ResponseEntity<String> response = userController.resetUserPassword(1);

        Assertions.assertThat(response.getBody()).isEqualTo("userNewPassword");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).resetUserPassword(1);
    }

}

