package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserAttemptsDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.ChangeUserRoleDTO;
import com.alfonsoristorato.lucreziaspresentbackend.model.NewUserRequestDTO;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

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
        requestSpecification(endpoint, "userFirstLogin")
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
        requestSpecification(endpoint, "user")
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(403)
                .body(equalTo("Access Denied"));
    }

    @ParameterizedTest
    @MethodSource("endpointsThatRequireFirstLoginFalseAndAdmin")
    @DisplayName("Pre Authorization:: blocks requests to certain endpoints if admin logged has not changed the default Password")
    void preAuthorization_blocksRequestsForAuthenticatedAdminIfDefaultPasswordNotChanged(String method, String endpoint) {
        requestSpecification(endpoint, "adminFirstLogin")
                .when()
                .request(method, endpoint)
                .then()
                .statusCode(403)
                .body(equalTo("Access Denied"));
    }

    private RequestSpecification requestSpecification(String endpoint, String username) {
        RequestSpecification requestSpecification = client.request()
                .auth()
                .basic(username, "defaultPass");
        switch (endpoint) {
            case "/user/role/110" ->
                    requestSpecification = requestSpecification.body(new ChangeUserRoleDTO("doesNotMatter"));
            case "/user/attempts/110" -> requestSpecification = requestSpecification.body(new ChangeUserAttemptsDTO(0));
            case "/user" -> requestSpecification = requestSpecification.body(new NewUserRequestDTO("doesNotMatter"));
            case "/entry", "/entry/110" -> requestSpecification = requestSpecification
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", "name")
                    .multiPart("content", "content")
                    .multiPart("title", "title")
                    .multiPart("icon", "1")
                    .multiPart("color", "red")
                    .multiPart("date", "2023-10-10");
        }
        return requestSpecification;

    }
}
