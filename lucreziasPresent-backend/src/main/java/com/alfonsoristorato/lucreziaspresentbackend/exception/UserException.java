package com.alfonsoristorato.lucreziaspresentbackend.exception;

import com.alfonsoristorato.lucreziaspresentbackend.model.UserError;

public class UserException extends RuntimeException{

    private final UserError error;

    public UserException(UserError error) {
        this.error = error;
    }
    public UserError getError(){return error;}
}
