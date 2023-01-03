package com.alfonsoristorato.lucreziaspresentbackend.repository;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
