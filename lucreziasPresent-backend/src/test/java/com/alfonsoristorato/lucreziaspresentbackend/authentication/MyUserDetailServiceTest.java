package com.alfonsoristorato.lucreziaspresentbackend.authentication;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private UserRepository userRepository;

    private final User user = User.builder()
            .id(1L)
            .username("username")
            .password("password")
            .role("role")
            .attempts(0)
            .firstLogin(false)
            .build();

    @Test
    void loadUserByUsername_returnsMyUserDetails() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        MyUserDetails myUserDetails = myUserDetailsService.loadUserByUsername("username");

        Assertions.assertThat(myUserDetails.getUser()).isEqualTo(user);
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundExceptionIfUserNotFoundInDb() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(()->myUserDetailsService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with the given username");
    }

}
