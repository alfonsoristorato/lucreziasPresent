package com.alfonsoristorato.lucreziaspresentbackend.exception;

import com.alfonsoristorato.lucreziaspresentbackend.model.UserError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> defaultExceptions(Exception ex) {
        log.error("Caught the following exception: {} with message: {}", ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserError> userException(UserException ex) {
        log.error("Caught the following exception: {} with message: {}", ex.getClass(), ex.getUsererror());
        return new ResponseEntity<>(ex.getUsererror(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
