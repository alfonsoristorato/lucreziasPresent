package com.alfonsoristorato.lucreziaspresentbackend.controller;

import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserAttemptsDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserRoleDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.NewUserRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

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
        List<User> userList = List.of(new User((long)1,"user", "password", "admin", 0, false));
        when(userService.getAllUsers()).thenReturn(userList);
        ResponseEntity<List<User>> response = userController.getAllUsers();
        Assertions.assertAll(
                () -> Assertions.assertEquals(userList, Objects.requireNonNull(response.getBody())),
                () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()));
        verify(userService).getAllUsers();
    }

    @Test
    void editUserRoleReturnsAStringAndOk_and_callsUserService() {
        ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO("user");
        when(userService.editUserRole(1, changeUserRoleDTO)).thenReturn("User role updated.");
        ResponseEntity<String> response = userController.editUserRole(1, changeUserRoleDTO);
        Assertions.assertAll(
                () -> Assertions.assertEquals("User role updated.", Objects.requireNonNull(response.getBody())),
                () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()));
        verify(userService).editUserRole(1, changeUserRoleDTO);
    }

    @Test
    void editUserAttemptsReturnsAStringAndOk_and_callsUserService() {
        ChangeUserAttemptsDTO changeUserAttemptsDTO = new ChangeUserAttemptsDTO(0);
        when(userService.editUserAttempts(1, changeUserAttemptsDTO)).thenReturn("User attempts updated.");
        ResponseEntity<String> response = userController.editUserAttempts(1, changeUserAttemptsDTO);
        Assertions.assertAll(
                () -> Assertions.assertEquals("User attempts updated.", Objects.requireNonNull(response.getBody())),
                () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()));
        verify(userService).editUserAttempts(1, changeUserAttemptsDTO);
    }

    @Test
    void addUserReturnsAStringAndOk_and_callsUserService() {
        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO("newUserName");
        when(userService.addUser(newUserRequestDTO)).thenReturn("userPassword");
        ResponseEntity<String> response = userController.addUser(newUserRequestDTO);
        Assertions.assertAll(
                () -> Assertions.assertEquals("userPassword", Objects.requireNonNull(response.getBody())),
                () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()));
        verify(userService).addUser(newUserRequestDTO);
    }

    @Test
    void resetUserPasswordReturnsAStringAndOk_and_callsUserService() {
        when(userService.resetUserPassword(1)).thenReturn("userNewPassword");
        ResponseEntity<String> response = userController.resetUserPassword(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals("userNewPassword", Objects.requireNonNull(response.getBody())),
                () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()));
        verify(userService).resetUserPassword(1);
    }

}

