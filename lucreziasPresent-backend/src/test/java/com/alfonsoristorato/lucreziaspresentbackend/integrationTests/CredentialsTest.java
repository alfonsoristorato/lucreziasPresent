package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.model.LoginRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CredentialsTest extends IntegrationTestsConfig {


    @Autowired
    private HttpClient client;

    @Test
    @DisplayName("Login:: right credentials")
    public void login_withRightCredentials() {
        LoginRequestDTO requestBody = new LoginRequestDTO("admin", "defaultPass");
        client.request()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body(
                        "size()", equalTo(5),
                        "id", notNullValue(),
                        "username", equalTo("admin"),
                        "role", equalTo("admin"),
                        "attempts", equalTo(0),
                        "firstLogin", equalTo(false)
                );
    }

    @Test
    @DisplayName("Login:: wrong credentials")
    public void login_withWrongCredentials() {
        LoginRequestDTO requestBody = new LoginRequestDTO("notAnUser", "wrongPassword");
        client.request()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(500)
                .body(
                        "size()", equalTo(2),
                        "description", equalTo("User error"),
                        "details", equalTo("Credenziali non riconosciute.")
                );
    }

    @Test
    @DisplayName("Login:: correct username and wrong password, locks an account after 4 times")
    public void login_withWrongPasswordMultipleTimes_accountGetsLocked() {
        LoginRequestDTO requestBody = new LoginRequestDTO("userToLock", "wrongPassword");
        IntStream.rangeClosed(1, 4).forEach(repetition -> {
            client.request()
                    .body(requestBody)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(500)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("User error"),
                            "details", equalTo("Credenziali non riconosciute.")
                    );
        });
        client.request()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(500)
                .body(
                        "size()", equalTo(2),
                        "description", equalTo("User error"),
                        "details", equalTo("Account bloccato, contatta l'amministratore per sbloccarlo.")
                );

    }

}
