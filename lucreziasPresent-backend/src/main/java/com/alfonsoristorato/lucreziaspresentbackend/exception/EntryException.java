package com.alfonsoristorato.lucreziaspresentbackend.exception;

import com.alfonsoristorato.lucreziaspresentbackend.model.EntryError;
import com.alfonsoristorato.lucreziaspresentbackend.model.UserError;

public class EntryException extends RuntimeException{

    private final EntryError error;

    public EntryException(EntryError error) {
        this.error = error;
    }
    public EntryError getError(){return error;}
}
