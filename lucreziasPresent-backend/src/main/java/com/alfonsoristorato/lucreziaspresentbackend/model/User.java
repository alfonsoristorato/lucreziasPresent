package com.alfonsoristorato.lucreziaspresentbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
