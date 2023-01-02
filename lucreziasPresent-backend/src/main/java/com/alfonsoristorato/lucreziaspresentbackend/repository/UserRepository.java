package com.alfonsoristorato.lucreziaspresentbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alfonsoristorato.lucreziaspresentbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
