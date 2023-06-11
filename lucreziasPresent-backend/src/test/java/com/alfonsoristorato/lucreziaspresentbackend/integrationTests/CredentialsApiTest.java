package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.model.LoginRequestDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.PasswordChangeRequestDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CredentialsApiTest extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    @Nested
    @DisplayName("Login tests")
    class loginTests {
        @Test
        @DisplayName("Login:: right credentials")
        void login_withRightCredentials() {
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
        void login_withWrongCredentials() {
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
        void login_withWrongPasswordMultipleTimes_accountGetsLocked() {
            LoginRequestDTO requestBody = new LoginRequestDTO("userToLock", "wrongPassword");
            IntStream.rangeClosed(1, 4).forEach(repetition ->
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
                            ));
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

    @Nested
    @DisplayName("Change Password tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class changePasswordTests {

        @Test
        @Order(1)
        @DisplayName("Change Password:: right credentials, valid new password")
        void changePassword_withRightCredentials_andValidNewPassword() {
            PasswordChangeRequestDTO requestBody = new PasswordChangeRequestDTO("userPassChange", "defaultPass", "newPassword123*!");

            client.request()
                    .auth()
                    .basic("userPassChange", "defaultPass")
                    .body(requestBody)
                    .when()
                    .post("/change-password")
                    .then()
                    .statusCode(200)
                    .body(
                            equalTo("Password Cambiata.")
                    );
        }

        @Test
        @DisplayName("Change Password:: right credentials, valid new password, different user in body")
        void changePassword_withRightCredentials_andValidNewPassword_differentUserInBody() {
            PasswordChangeRequestDTO requestBody = new PasswordChangeRequestDTO("notTheSameUser", "defaultPass", "newPassword123*!");

            client.request()
                    .auth()
                    .basic("userPassChange", "newPassword123*!")
                    .body(requestBody)
                    .when()
                    .post("/change-password")
                    .then()
                    .statusCode(500)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("Disallowed change"),
                            "details", equalTo("You cannot change the password for another user.")
                    );
        }

        @Test
        @DisplayName("Change Password:: right credentials, new password same as old")
        void changePassword_withRightCredentials_newPasswordSameAsOld() {
            PasswordChangeRequestDTO requestBody = new PasswordChangeRequestDTO("userPassChange", "newPassword123*!", "newPassword123*!");

            client.request()
                    .auth()
                    .basic("userPassChange", "newPassword123*!")
                    .body(requestBody)
                    .when()
                    .post("/change-password")
                    .then()
                    .statusCode(500)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("User error"),
                            "details", equalTo("La nuova password deve essere diversa dalla vecchia.")
                    );
        }

        @ParameterizedTest
        @ValueSource(strings = {"weak", "mediumPass"})
        @DisplayName("Change Password:: right credentials, new password not strong")
        void changePassword_withRightCredentials_newPasswordNotStrong(String newPass) {
            PasswordChangeRequestDTO requestBody = new PasswordChangeRequestDTO("userPassChange", "newPassword123*!", newPass);
            String newPasswordStrength = newPass.equals("weak") ? "'Debole'" : "'Media'";

            client.request()
                    .auth()
                    .basic("userPassChange", "newPassword123*!")
                    .body(requestBody)
                    .when()
                    .post("/change-password")
                    .then()
                    .statusCode(500)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("User error"),
                            "details", equalTo(String.format("La nuova password ha una sicurezza di tipo: %s. Riprova e assicurati che sia più sicura.", newPasswordStrength))
                    );
        }

        @Test
        @DisplayName("Change Password:: right credentials, old Password not valid")
        void changePassword_withRightCredentials_oldPasswordNotValid() {
            PasswordChangeRequestDTO requestBody = new PasswordChangeRequestDTO("userPassChange", "notValid", "notMatter");

            client.request()
                    .auth()
                    .basic("userPassChange", "newPassword123*!")
                    .body(requestBody)
                    .when()
                    .post("/change-password")
                    .then()
                    .statusCode(500)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("User error"),
                            "details", equalTo("Credenziali non riconosciute.")
                    );
        }


    }


}
