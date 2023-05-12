package com.alfonsoristorato.lucreziaspresentbackendft.util;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Component
public class HttpClient {
    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    @Value("${app.url}")
    private String baseUri;

    public void resetSpecification() {
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(baseUri).addHeader("Content-Type","application/json");
    }

    public Response sendGetRequest(String endpoint, Map<String,String> headers) {
        resetSpecification();
        RequestSpecification requestSpecification = requestSpecBuilder.build().headers(headers);
        return given(requestSpecification).get(endpoint);
    }

    public Response sendPostRequest(String endpoint, Map<String,String> headers, String body) {
        resetSpecification();
        RequestSpecification requestSpecification = requestSpecBuilder.build().headers(headers).body(body);
        return given(requestSpecification).post(endpoint);
    }

    public Response sendPatchRequest(String endpoint, Map<String,String> headers, String body, String pathParams) {
        resetSpecification();
        RequestSpecification requestSpecification = requestSpecBuilder.build().headers(headers).body(body);
        return given(requestSpecification).patch(endpoint,pathParams);
    }

    public void addHeader(String header, String value) {
        requestSpecBuilder.addHeader(header, value);
    }

    public void addAuthorizationHeader(String username, String password, Map<String,String> headers) {
        String encodedToken =
                Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        headers.put("Authorization", "Basic " + encodedToken);
    }
}
