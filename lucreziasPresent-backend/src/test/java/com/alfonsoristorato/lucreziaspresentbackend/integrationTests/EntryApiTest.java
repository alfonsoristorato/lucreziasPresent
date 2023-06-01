package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.model.EntryFormWrapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Map;

public class EntryApiTest extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    @Test
    @DisplayName("Entry endpoint:: POST /entry with form")
    void addEntry_returns201IfBodyIsPassed() {
        Map<String, String> headers = client.createAuthorizationHeader("user", "defaultPass");

        client.request()
                .headers(headers)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("name","name")
                .multiPart("content","content")
                .multiPart("title","title")
                .multiPart("icon","1")
                .multiPart("color","red")
                .multiPart("date","2023-10-10")
                .when()
                .post("/entry")
                .then()
                .statusCode(201)
                .body(Matchers.blankOrNullString());
    }

    @Test
    @DisplayName("Entry endpoint:: POST /entry without form")
    void addEntry_returns500IfNoBodyIsPassed() {
        Map<String, String> headers = client.createAuthorizationHeader("user", "defaultPass");

        client.request()
                .headers(headers)
                .when()
                .post("/entry")
                .then()
                .statusCode(500)
                .body(Matchers.equalTo("Unexpected Error."));
    }

}
