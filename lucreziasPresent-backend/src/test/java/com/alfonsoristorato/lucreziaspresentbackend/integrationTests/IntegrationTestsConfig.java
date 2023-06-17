package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import com.alfonsoristorato.lucreziaspresentbackend.LucreziasPresentBackendApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LucreziasPresentBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public abstract class IntegrationTestsConfig {
}
