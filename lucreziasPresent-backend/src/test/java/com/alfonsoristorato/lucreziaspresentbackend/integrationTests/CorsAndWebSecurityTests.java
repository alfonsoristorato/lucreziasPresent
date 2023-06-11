package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;

public class CorsAndWebSecurityTests extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    public static Stream<Arguments> endpointsSourceNotLogin() {
        return Stream.of(
                Arguments.of("POST", "/change-password"),
                Arguments.of("GET", "/entry"),
                Arguments.of("POST", "/entry"),
                Arguments.of("PATCH", "/entry/110"),
                Arguments.of("DELETE", "/entry/110"),
                Arguments.of("GET", "/user"),
                Arguments.of("PATCH", "/user/role/110"),
                Arguments.of("PATCH", "/user/attempts/110"),
                Arguments.of("POST", "/user"),
                Arguments.of("PATCH", "/user/reset-password/110")
        );
    }

    public static Stream<Arguments> allEndpoints() {
        return Stream.concat(
                Stream.of(Arguments.of("POST", "/login")),
                endpointsSourceNotLogin()
        );
    }

    @Nested
    @DisplayName("Web Security tests")
    class webSecurityTests {
        @ParameterizedTest
        @MethodSource("com.alfonsoristorato.lucreziaspresentbackend.integrationTests.CorsAndWebSecurityTests#endpointsSourceNotLogin")
        @DisplayName("Web Security:: blocks requests non authenticated to all endpoints but /login")
        void filterChain_blocksRequestsNonAuthenticated(String method, String endpoint) {
            client.request()
                    .when()
                    .request(method, endpoint)
                    .then()
                    .statusCode(401);
        }

        @Test
        @DisplayName("Web Security:: allows non authenticated requests to /login")
        void filterChain_allowsNonAuthenticatedRequestsToLoginEndpoint() {
            client.request()
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(500)
                    .body(equalTo("Unexpected Error."));
        }
    }

    @Nested
    @DisplayName("CORS tests")
    class corsTests {
        @ParameterizedTest
        @MethodSource("com.alfonsoristorato.lucreziaspresentbackend.integrationTests.CorsAndWebSecurityTests#allEndpoints")
        @DisplayName("CORS:: blocks requests from non-allowed origin")
        void filterChain_blocksRequestsFromNonAllowedOrigins(String method, String endpoint) {
            client.request()
                    .when()
                    .header("Origin", "http://notAllowed")
                    .request(method, endpoint)
                    .then()
                    .statusCode(403)
                    .body(equalTo("Invalid CORS request"));
        }
    }
}
