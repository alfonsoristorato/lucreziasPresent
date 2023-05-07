package com.alfonsoristorato.lucreziaspresentbackend.services;

import com.alfonsoristorato.lucreziaspresentbackend.authentication.MyUserDetails;
import com.alfonsoristorato.lucreziaspresentbackend.exception.UserException;
import com.alfonsoristorato.lucreziaspresentbackend.model.*;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import com.alfonsoristorato.lucreziaspresentbackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService myUserDetails;

    @Mock
    private Principal principal;


    @Test
    void validUsernameAndPassword_returnsAnOptionalOfUser() {
        String passEncoded = passwordEncoder.encode("password");
        User user = new User(1L, "name", passEncoded, "admin", 0, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        User userReturned = userService.validUsernameAndPassword("name", "password");

        Assertions.assertEquals(user, userReturned);
        verify(userRepository).findByUsername("name");
        verify(myUserDetails).loadUserByUsername("name");
        verify(passwordEncoder).matches("password", user.getPassword());
        verify(userRepository).save(user);

    }

    @Test
    void validUsernameAndPassword_throwsUserExceptionIfUserIsNotFound() {
        when(userRepository.findByUsername("name")).thenReturn(Optional.empty());

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.validUsernameAndPassword("name", "password"));

        Assertions.assertEquals(UserError.USER_ERROR("Credenziali non riconosciute."), ex.getUsererror());
        verify(userRepository).findByUsername("name");
        verify(myUserDetails, never()).loadUserByUsername("name");
        verify(passwordEncoder, never()).matches(eq("password"), any());
        verify(userRepository, never()).save(any());

    }

    @Test
    void validUsernameAndPassword_throwsUserExceptionIfPasswordsDontMatch() {
        String passEncoded = passwordEncoder.encode("password");
        User user = new User(1L, "name", passEncoded, "admin", 0, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.validUsernameAndPassword("name", "wrongPassword"));

        Assertions.assertEquals(UserError.USER_ERROR("Credenziali non riconosciute."), ex.getUsererror());
        verify(userRepository).findByUsername("name");
        verify(myUserDetails).loadUserByUsername("name");
        verify(passwordEncoder).matches("wrongPassword", user.getPassword());
        verify(userRepository).save(user);

    }

    @Test
    void validUsernameAndPassword_throwsUserExceptionIfAccountIsLocked() {
        String passEncoded = passwordEncoder.encode("password");
        User user = new User(1L, "name", passEncoded, "admin", 4, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.validUsernameAndPassword("name", "password"));

        Assertions.assertEquals(UserError.USER_ERROR("Account bloccato, contatta l'amministratore per sbloccarlo."), ex.getUsererror());
        verify(userRepository).findByUsername("name");
        verify(myUserDetails).loadUserByUsername("name");
        verify(passwordEncoder, never()).matches(eq("password"), any());
        verify(userRepository, never()).save(user);

    }

    @Test
    void changePassword_returnsExpectedMessage() {
        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO("name", "password", "passworD123*!");
        String passEncoded = passwordEncoder.encode("password");
        User user = new User(1L, "name", passEncoded, "admin", 0, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(principal.getName()).thenReturn("name");

        String response = userService.changePassword(passwordChangeRequestDTO, principal);

        Assertions.assertEquals("Password Cambiata.", response);
        verify(userRepository, times(2)).save(user);
    }

    @Test
    void changePassword_throwsUserExceptionIfPrincipalNameAndUsernameInDTODontMatch() {
        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO("name", "passworD123*!", "newPassword");

        when(principal.getName()).thenReturn("differentUser");

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.changePassword(passwordChangeRequestDTO, principal));

        Assertions.assertEquals(UserError.DISALLOWED_CHANGE("You cannot change the password for another user."), ex.getUsererror());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_throwsUserExceptionIfNewPasswordIsSameAsOld() {
        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO("name", "passworD123*!", "passworD123*!");
        String passEncoded = passwordEncoder.encode("passworD123*!");
        User user = new User(1L, "name", passEncoded, "admin", 0, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));
        when(passwordEncoder.matches("passworD123*!", user.getPassword())).thenReturn(true);
        when(principal.getName()).thenReturn("name");

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.changePassword(passwordChangeRequestDTO, principal));

        Assertions.assertEquals(UserError.USER_ERROR("La nuova password deve essere diversa dalla vecchia."), ex.getUsererror());
        verify(userRepository).save(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"mediumPass12", "weak"})
    void changePassword_throwsUserExceptionIfNewPasswordIsNotStrong(String newPassword) {
        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO("name", "password", newPassword);
        String passEncoded = passwordEncoder.encode("password");
        User user = new User(1L, "name", passEncoded, "admin", 0, false);
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        when(myUserDetails.loadUserByUsername("name")).thenReturn(new MyUserDetails(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(principal.getName()).thenReturn("name");

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.changePassword(passwordChangeRequestDTO,principal));
        String exceptionMessage = "La nuova password ha una sicurezza di tipo: " + (newPassword.equals("weak") ? "'Debole'" : "'Media'")
                + ". Riprova e assicurati che sia più sicura.";
        Assertions.assertEquals(UserError.USER_ERROR(exceptionMessage), ex.getUsererror());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetUserPassword_returnsANewPassword() {
        User user = new User(1L, "name", "password", "admin", 0, false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String newPassword = userService.resetUserPassword(1);

        Assertions.assertNotEquals("password", newPassword);
        verify(userRepository).save(user);
    }

    @Test
    void resetUserPassword_throwsUserExceptionIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.resetUserPassword(1));

        Assertions.assertEquals(UserError.USER_NOT_FOUND(), ex.getUsererror());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_returnsAListOfUsers() {
        User user = new User(1L, "name", "password", "admin", 0, false);
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> listReturned = userService.getAllUsers();

        Assertions.assertEquals(List.of(user), listReturned);
    }

    @Test
    void editUserRole_returnsExpectedMessage() {
        ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO("admin");
        User user = new User(1L, "name", "password", "user", 4, false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String response = userService.editUserRole(1, changeUserRoleDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals("admin", user.getRole()),
                () -> Assertions.assertEquals("User role updated.", response)
        );
        verify(userRepository).save(user);
    }

    @Test
    void editUserRole_throwsUserExceptionIfUserNotFound() {
        ChangeUserRoleDTO changeUserRoleDTO = new ChangeUserRoleDTO("admin");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.editUserRole(1, changeUserRoleDTO));

        Assertions.assertEquals(UserError.USER_NOT_FOUND(), ex.getUsererror());
        verify(userRepository, never()).save(any());
    }

    @Test
    void editUserAttempts_returnsExpectedMessage() {
        ChangeUserAttemptsDTO changeUserAttemptsDTO = new ChangeUserAttemptsDTO(0);
        User user = new User(1L, "name", "password", "admin", 4, false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String response = userService.editUserAttempts(1, changeUserAttemptsDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, user.getAttempts()),
                () -> Assertions.assertEquals("User attempts updated.", response)
        );
        verify(userRepository).save(user);
    }

    @Test
    void editUserAttempts_throwsUserExceptionIfUserNotFound() {
        ChangeUserAttemptsDTO changeUserAttemptsDTO = new ChangeUserAttemptsDTO(0);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.editUserAttempts(1, changeUserAttemptsDTO));

        Assertions.assertEquals(UserError.USER_NOT_FOUND(), ex.getUsererror());
        verify(userRepository, never()).save(any());
    }

    @Test
    void addUser_returnsRandomPassword() {
        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO("newUser");

        String response = userService.addUser(newUserRequestDTO);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(response),
                () -> Assertions.assertTrue(response.length() >= 8)

        );
        verify(userRepository).save(any());
    }

    @Test
    void addUser_throwsUserExceptionIfUserAlreadyExists() {
        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO("newUser");
        User user = new User(1L, "newUser", "password", "user", 0, false);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.of(user));

        UserException ex = Assertions.assertThrows(UserException.class,
                () -> userService.addUser(newUserRequestDTO));

        Assertions.assertEquals(UserError.USER_ERROR("User already exists."), ex.getUsererror());
        verify(userRepository, never()).save(user);
    }
}
