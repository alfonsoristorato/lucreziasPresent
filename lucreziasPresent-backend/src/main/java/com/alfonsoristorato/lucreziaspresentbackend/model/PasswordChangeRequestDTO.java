package com.alfonsoristorato.lucreziaspresentbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordChangeRequestDTO extends LoginRequestDTO {
    private String newPassword;
    public PasswordChangeRequestDTO(String username, String password, String newPassword) {
        super(username, password);
        this.newPassword = newPassword;
    }
}
