package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

public class CorsAndWebSecurityTests extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    @Nested
    @DisplayName("Web Security tests")
    class webSecurityTests {
        @ParameterizedTest
        @CsvSource({
                "POST, /change-password",
                "GET, /entry",
                "POST, /entry",
                "PATCH, /entry/110",
                "DELETE, /entry/110",
                "GET, /user",
                "PATCH, /user/role/110",
                "PATCH, /user/attempts/110",
                "POST, /user",
                "DELETE, /user/reset-password/110"
        })
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
                    .statusCode(500);
        }
    }
}
