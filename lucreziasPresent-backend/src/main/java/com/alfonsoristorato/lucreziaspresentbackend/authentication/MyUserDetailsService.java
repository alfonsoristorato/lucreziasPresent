package com.alfonsoristorato.lucreziaspresentbackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import com.alfonsoristorato.lucreziaspresentbackend.repository.UserRepository;

public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with the given username");
        }

        return new MyUserDetails(user);
    }
}
