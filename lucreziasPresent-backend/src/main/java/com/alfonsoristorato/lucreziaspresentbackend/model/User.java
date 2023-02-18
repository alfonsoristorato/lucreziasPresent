package com.alfonsoristorato.lucreziaspresentbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @JsonIgnore
    private String password;

    private String role;

    private int attempts;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean firstLogin;
}
