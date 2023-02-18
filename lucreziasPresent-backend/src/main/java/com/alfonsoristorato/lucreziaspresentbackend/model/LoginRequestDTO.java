package com.alfonsoristorato.lucreziaspresentbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String username;

    private String password;
}
