package com.alfonsoristorato.lucreziaspresentbackend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordChangeRequest extends LoginRequest {
    private String newPassword;
}
