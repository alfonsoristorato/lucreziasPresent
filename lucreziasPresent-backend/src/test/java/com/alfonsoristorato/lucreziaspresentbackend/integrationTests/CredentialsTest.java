package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CredentialsTest extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    @Test
    public void login() {
        Map<String, String> body = Map.of("username", "admin", "password", "defaultPass");
        client.request()
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("id"));
    }
}
