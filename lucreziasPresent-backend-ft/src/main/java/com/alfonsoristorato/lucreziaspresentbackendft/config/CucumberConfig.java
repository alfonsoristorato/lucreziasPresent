package com.alfonsoristorato.lucreziaspresentbackendft.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
@ContextConfiguration(
        classes = {ComponentConfig.class},
        initializers = ConfigDataApplicationContextInitializer.class)
@CucumberContextConfiguration
public class CucumberConfig {
}

