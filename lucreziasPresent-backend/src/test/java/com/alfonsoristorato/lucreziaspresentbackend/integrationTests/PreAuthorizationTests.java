package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserAttemptsDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserRoleDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.NewUserRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;

public class PreAuthorizationTests extends IntegrationTestsConfig {
    @Autowired
    private HttpClient client;

    public static Stream<Arguments> endpointsThatRequireFirstLoginFalseOnly() {
        return Stream.of(
                Arguments.of("GET", "/entry"),
                Arguments.of("POST", "/entry"),
                Arguments.of("PATCH", "/entry/110"),
                Arguments.of("DELETE", "/entry/110")
        );
    }

    public static Stream<Arguments> endpointsThatRequireFirstLoginFalseAndAdmin() {
        return Stream.of(
                Arguments.of("GET", "/user"),
                Arguments.of("PATCH", "/user/role/110"),
                Arguments.of("PATCH", "/user/attempts/110"),
                Arguments.of("POST", "/user"),
                Arguments.of("PATCH", "/user/reset-password/110")
        );
    }

    @ParameterizedTest
    @MethodSource("endpointsThatRequireFirstLoginFalseOnly")
    @DisplayName("Pre Authorization:: blocks requests to certain endpoints if user logged in has not changed the default Password")
    void preAuthorization_blocksRequestsForAuthenticatedUsersIfDefaultPasswordNotChanged(String method, String endpoint) {
        Map<String, String> headers = client.createAuthorizationHeader("userFirstLogin", "defaultPass");

        client.request()
                .headers(headers)
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(403)
                .body(equalTo("Access Denied"));
    }

    @ParameterizedTest
    @MethodSource("endpointsThatRequireFirstLoginFalseAndAdmin")
    @DisplayName("Pre Authorization:: blocks requests to certain endpoints if user logged in is not an admin")
    void preAuthorization_blocksRequestsForAuthenticatedUsersIfTryingToAccessAdminOnlyEndpoints(String method, String endpoint) {
        Map<String, String> headers = client.createAuthorizationHeader("user", "defaultPass");
        Object body;
        switch (endpoint) {
            case "/user/role/110" -> body = new ChangeUserRoleDTO("doesNotMatter");
            case "/user/attempts/110" -> body = new ChangeUserAttemptsDTO(0);
            case "/user" -> body = new NewUserRequestDTO("doesNotMatter");
            default -> body = "";
        }

        client.request()
                .headers(headers)
                .body(body)
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(403)
                .body(equalTo("Access Denied"));
    }

    @ParameterizedTest
    @MethodSource("endpointsThatRequireFirstLoginFalseAndAdmin")
    @DisplayName("Pre Authorization:: blocks requests to certain endpoints if user logged in is not an admin")
    void preAuthorization_blocksRequestsForAuthenticatedAdminIfDefaultPasswordNotChanged(String method, String endpoint) {
        Map<String, String> headers = client.createAuthorizationHeader("adminFirstLogin", "defaultPass");
        Object body;
        switch (endpoint) {
            case "/user/role/110" -> body = new ChangeUserRoleDTO("doesNotMatter");
            case "/user/attempts/110" -> body = new ChangeUserAttemptsDTO(0);
            case "/user" -> body = new NewUserRequestDTO("doesNotMatter");
            default -> body = "";
        }

        client.request()
                .headers(headers)
                .body(body)
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(403)
                .body(equalTo("Access Denied"));
    }
}
