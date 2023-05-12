package com.alfonsoristorato.lucreziaspresentbackendft;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/main/resources/",
        glue = "com.alfonsoristorato.lucreziaspresentbackendft")
public class RunCucumberTest {
    public static void main(String[] args) {
        String[] newArgs = new String[]{
                "--plugin", "pretty",
                "--glue", "com.alfonsoristorato.lucreziaspresentbackendft",
                "classpath:features"
        };

        io.cucumber.core.cli.Main.main(newArgs);
    }
}
