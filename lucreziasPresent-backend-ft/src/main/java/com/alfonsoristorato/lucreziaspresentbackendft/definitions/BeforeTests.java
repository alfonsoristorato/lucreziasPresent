package com.alfonsoristorato.lucreziaspresentbackendft.definitions;

import com.alfonsoristorato.lucreziaspresentbackendft.util.HttpClient;
import io.cucumber.java.Before;


public class BeforeTests {
    private final HttpClient httpClient;

    public BeforeTests(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Before
    public void reset() {
        httpClient.resetSpecification();
    }
}
