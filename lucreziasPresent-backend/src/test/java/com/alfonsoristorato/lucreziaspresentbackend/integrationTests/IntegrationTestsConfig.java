package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.LucreziasPresentBackendApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LucreziasPresentBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public abstract class IntegrationTestsConfig {
}
