package com.alfonsoristorato.lucreziaspresentbackendft.definitions;

import com.alfonsoristorato.lucreziaspresentbackendft.util.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.assertj.core.api.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestSteps {
    private final ObjectMapper mapper = new ObjectMapper();


    private static final Map<String, String> ENDPOINTS = Map.ofEntries(
            Map.entry("login", "/login"),
            Map.entry("change-password", "/change-password"),
            Map.entry("user", "/user"),
            Map.entry("user/edit-role", "/user/role/{userId}"),
            Map.entry("user/edit-attempts", "/user/attempts/{userId}"),
            Map.entry("user/reset-password", "/user/reset-password/{userId}")
    );
    Response response;

    String pathParams;
    private final HttpClient client;

    private final Map<String, String> headers = new HashMap<>();

    public RequestSteps(HttpClient client) {
        this.client = client;
    }

    /**
     * @param newPathParams username mapped to relevant ID according to feed-functional.sql.
     *                      If not matching any return ID 0 which does not exist in db.
     */
    @Given("The next request contains the following path parameters: {string}")
    public void clientsUsesTheFollowingPathParams(String newPathParams) {
        switch (newPathParams){
            case "admin" -> pathParams = "101";
            case "user" -> pathParams = "102";
            case "userPassChange" -> pathParams = "103";
            case "userRoleChange" -> pathParams = "104";
            case "adminFirstLogin" -> pathParams = "105";
            case "userFirstLogin" -> pathParams = "106";
            case "adminLocked" -> pathParams = "107";
            case "userLocked" -> pathParams = "108";
            case "userToLock" -> pathParams = "109";
            case "userResetPassword" -> pathParams = "110";
            default -> pathParams = "0";
        }
    }

    @When("Client makes a {string} request to {string} endpoint( with the following body)")
    public void clientMakesARequestWithBody(
            String method, String endpoint, Map<String, String> body) throws Exception {
        String bodyConverted = mapper.writeValueAsString(body);
        response = switch (method) {
            case "POST" -> client.sendPostRequest(ENDPOINTS.get(endpoint), headers, bodyConverted);
            case "PATCH" -> client.sendPatchRequest(ENDPOINTS.get(endpoint), headers, bodyConverted, pathParams);
            case "GET" -> client.sendGetRequest(ENDPOINTS.get(endpoint), headers);
            default -> throw new Exception("Method not allowed");
        };
    }

    @And("Client receives a status code of {int}")
    public void clientReceivesAStatusCodeOf(int code) {
        Assertions.assertThat(code).isEqualTo(response.getStatusCode());
    }

    @And("The response has the following properties")
    public void theResponseHasTheFollowingProperties(Map<String, String> expectedBody) throws JsonProcessingException {
        Map<String, String> actualBody = mapper.readValue(response.getBody().asString(), new TypeReference<>() {
        });

        Assertions.assertThat(actualBody).containsAllEntriesOf(expectedBody);
    }

    @Given("Client provides following authentication credentials")
    public void clientProvidesCredentials(Map<String, String> authCredentials) {
        client.addAuthorizationHeader(authCredentials.get("username"), authCredentials.get("password"), headers);
    }

    @Given("Client provides {string} {string} authentication credentials")
    public void clientProvidesAdminCredentials(String credentialsType, String username) {
        if (credentialsType.equals("valid")) {
            client.addAuthorizationHeader(username, "defaultPass", headers);
        } else {
            client.addAuthorizationHeader("invalidUsername", "invalidPassword", headers);

        }
    }

    @And("The response has the following text: {string}")
    public void theResponseHasTheFollowingText(String expectedBody) {
        Assertions.assertThat(expectedBody).isEqualTo(response.getBody().asString());
    }

    @And("The response contains a list of {string}( including the user below)")
    public void theResponseContainsAListOfUsers(String listOf, Map<String, Object> body) throws JsonProcessingException {
        List<Map<String, Object>> actualBody = mapper.readValue(response.getBody().asString(), new TypeReference<>() {
        });
        switch (listOf) {
            case "users" -> {
                org.junit.jupiter.api.Assertions.assertAll(
                        () -> Assertions.assertThat(actualBody.size()).isGreaterThan(0),
                        () -> Assertions.assertThat(actualBody.stream().allMatch(userObject
                                -> userObject.containsKey("username")
                                && userObject.containsKey("role")
                                && userObject.containsKey("id")
                                && userObject.containsKey("attempts")
                                && userObject.containsKey("firstLogin"))).isTrue()
                );
                if (!body.containsKey("\"\"")) {
                    List<Map<String, String>> bodyToStringMap = mapper.readValue(mapper.writeValueAsString(actualBody), new TypeReference<>() {
                    });
                    Assertions.assertThat(bodyToStringMap).anyMatch(user -> user.entrySet().containsAll(body.entrySet()));
                }
            }
        }

    }

    @And("The response contains the following keys")
    public void theResponseContainsTheFollowingKeys(List<String> keys) throws JsonProcessingException {
        Map<String, String> actualBody = mapper.readValue(response.getBody().asString(), new TypeReference<>() {
        });
        Assertions.assertThat(actualBody.keySet().containsAll(keys)).isTrue();
    }

    @And("The response contains a new password with a total length higher than 8")
    public void responseContainsNewValidPassword() {
        String responseAsString = response.getBody().asString();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(responseAsString.isEmpty()).isFalse(),
                () -> Assertions.assertThat(responseAsString.length() > 8).isTrue()
        );
    }

    @Given("the headers are cleared")
    public void clientClearsHeaders() {
        headers.clear();
    }
}
