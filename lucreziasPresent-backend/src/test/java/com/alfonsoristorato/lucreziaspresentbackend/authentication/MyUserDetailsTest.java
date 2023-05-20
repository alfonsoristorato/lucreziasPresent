package com.alfonsoristorato.lucreziaspresentbackend.authentication;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;


public class MyUserDetailsTest {
    private final User user = User.builder()
            .id(1L)
            .username("username")
            .password("password")
            .role("role")
            .attempts(0)
            .firstLogin(false)
            .build();


    private final MyUserDetails myUserDetails = new MyUserDetails(user);

    @Test
    void getAuthorities_returnsListOfAuthorities() {
        Collection<? extends GrantedAuthority> authorities = myUserDetails.getAuthorities();
        Assertions.assertThat(authorities).hasSize(1);
        Assertions.assertThat(authorities.iterator().next()).isEqualTo(new SimpleGrantedAuthority(user.getRole()));
    }

    @Test
    void getPassword_returnsTheUserPassword() {
        String password = myUserDetails.getPassword();
        Assertions.assertThat(password).isEqualTo("password");
    }

    @Test
    void getUsername_returnsTheUserUsername() {
        String username = myUserDetails.getUsername();
        Assertions.assertThat(username).isEqualTo("username");
    }

    @Test
    void isAccountNonLocked_returnsTrueIfUserAttemptsLowerThan4() {
        boolean accountNonLocked = myUserDetails.isAccountNonLocked();
        Assertions.assertThat(accountNonLocked).isTrue();
    }

    @Test
    void isAccountNonLocked_returnsFalseIfUserAttemptsHigherThan3() {
        User lockedUser = user;
        lockedUser.setAttempts(4);
        MyUserDetails newMyUserDetails = new MyUserDetails(lockedUser);
        boolean accountNonLocked = newMyUserDetails.isAccountNonLocked();
        Assertions.assertThat(accountNonLocked).isFalse();
    }

}
