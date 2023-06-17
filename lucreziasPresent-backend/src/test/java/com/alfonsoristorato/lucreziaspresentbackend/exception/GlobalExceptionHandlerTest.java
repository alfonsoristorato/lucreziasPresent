package com.alfonsoristorato.lucreziaspresentbackend.exception;

import com.alfonsoristorato.lucreziaspresentbackend.model.EntryError;
import com.alfonsoristorato.lucreziaspresentbackend.model.UserError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void defaultExceptions_catchesAllExceptions(){
        ResponseEntity<String> response = globalExceptionHandler.defaultExceptions(new Exception("I threw"));
        Assertions.assertThat(response.getBody()).isEqualTo("Unexpected Error.");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void accessDeniedException_catchesAllAccessDeniedException(){
        ResponseEntity<String> response = globalExceptionHandler.accessDeniedException(new AccessDeniedException("Access denied"));
        Assertions.assertThat(response.getBody()).isEqualTo("Access denied");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @ParameterizedTest
    @MethodSource("userExceptionTestData")
    void userException_catchesAllUserExceptions_andReturnsExpectedStatusCode(UserError userError, HttpStatus httpStatus){
        ResponseEntity<UserError> response = globalExceptionHandler.userException(new UserException(userError));
        Assertions.assertThat(response.getBody()).isEqualTo(userError);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(httpStatus);
    }

    @ParameterizedTest
    @MethodSource("entryExceptionTestData")
    void entryException_catchesAllEntryExceptions_andReturnsExpectedStatusCode(EntryError entryError, HttpStatus httpStatus){
        ResponseEntity<EntryError> response = globalExceptionHandler.entryException(new EntryException(entryError));
        Assertions.assertThat(response.getBody()).isEqualTo(entryError);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(httpStatus);
    }


    private static Stream<Arguments> userExceptionTestData(){
        return Stream.of(
                Arguments.of(UserError.USER_NOT_FOUND(), HttpStatus.NOT_FOUND),
                Arguments.of(UserError.DISALLOWED_CHANGE(), HttpStatus.FORBIDDEN),
                Arguments.of(UserError.USER_ERROR("this is an error"), HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    private static Stream<Arguments> entryExceptionTestData(){
        return Stream.of(
                Arguments.of(EntryError.ENTRY_NOT_FOUND(), HttpStatus.NOT_FOUND),
                Arguments.of(EntryError.DISALLOWED_CHANGE(), HttpStatus.FORBIDDEN),
                Arguments.of(EntryError.ENTRY_ERROR("this is an error"), HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }
}
