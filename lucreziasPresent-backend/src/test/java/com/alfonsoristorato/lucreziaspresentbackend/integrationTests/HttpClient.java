package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class HttpClient {

    @Value("${app.url}")
    private String baseUri;

    public RequestSpecification request() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        RequestSpecification requestSpecification = requestSpecBuilder
                .setBaseUri(baseUri)
                .addHeader("Content-Type", "application/json")
                .build();
        return given(requestSpecification);
    }
}
