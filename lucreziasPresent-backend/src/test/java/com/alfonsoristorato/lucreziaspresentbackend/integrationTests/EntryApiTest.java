package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public class EntryApiTest extends IntegrationTestsConfig {

    @Autowired
    private HttpClient client;

    @Autowired
    private MockMvc mockMvc;

    private final MultiValueMap<String, String> formParamsWithoutFileMultiMap = new LinkedMultiValueMap<>() {
        {
            add("name", "name");
            add("content", "content");
            add("title", "title");
            add("icon", "1");
            add("color", "red");
            add("date", "2023-10-10");
        }
    };

    @Nested
    @DisplayName("Entry endpoint:: POST /entry")
    class addEntryTests {
        @Test
        @DisplayName("POST /entry with form and no file")
        void addEntry_returns201IfFormIsPassed() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", formParamsWithoutFileMultiMap.get("name").get(0))
                    .multiPart("content", formParamsWithoutFileMultiMap.get("content").get(0))
                    .multiPart("title", formParamsWithoutFileMultiMap.get("title").get(0))
                    .multiPart("icon", formParamsWithoutFileMultiMap.get("icon").get(0))
                    .multiPart("color", formParamsWithoutFileMultiMap.get("color").get(0))
                    .multiPart("date", formParamsWithoutFileMultiMap.get("date").get(0))
                    .when()
                    .post("/entry")
                    .then()
                    .statusCode(201)
                    .body(Matchers.blankOrNullString());
        }

        @ParameterizedTest
        @DisplayName("POST /entry with form and supported file")
        @CsvSource({
                "image.jpeg, image/jpeg",
                "image.png, image/png",
                "image.jpg, image/jpg",
                "image.gif, image/gif",
        })
        void addEntry_returns201IfFormWithFileIsPassed(String fileName, String mimeType) throws Exception {
            File testFile = createImageFile(fileName, fileName.substring(fileName.indexOf(".") + 1));
            byte[] testFileContent = Files.readAllBytes(testFile.toPath());
            MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, mimeType, testFileContent);

            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("/entry")
                            .file(multipartFile)
                            .params(formParamsWithoutFileMultiMap)
                            .with(httpBasic("user", "defaultPass")))
                    .andExpectAll(
                            MockMvcResultMatchers.status().isCreated(),
                            MockMvcResultMatchers.content().string(Matchers.blankOrNullString()));
        }

        @Test
        @DisplayName("POST /entry without form throws an Exception")
        void addEntry_returns500IfNoFormIsPassed() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .post("/entry")
                    .then()
                    .statusCode(500)
                    .body(Matchers.equalTo("Unexpected Error."));
        }

        @ParameterizedTest
        @DisplayName("POST /entry with form and non-supported file throws an Exception")
        @CsvSource({
                "file.mp4, video/mp4",
                "file.mp3, audio/mpeg3"
        })
        void addEntry_returns500IfFilePassedInFormIsNotSupported(String fileName, String mimeType) throws Exception {
            MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, mimeType, "fileContent".getBytes());

            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("/entry")
                            .file(multipartFile)
                            .params(formParamsWithoutFileMultiMap)
                            .with(httpBasic("user", "defaultPass")))
                    .andExpectAll(
                            MockMvcResultMatchers.status().is5xxServerError(),
                            MockMvcResultMatchers.content().json("{\"description\":\"Entry error\",\"details\":\"File type not accepted.\"}"));
        }

        @Test
        @DisplayName("POST /entry with incomplete form throws an Exception")
        void addEntry_returns500IfFormPassedIsIncomplete() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", formParamsWithoutFileMultiMap.get("name").get(0))
                    .multiPart("content", formParamsWithoutFileMultiMap.get("content").get(0))
                    .multiPart("title", formParamsWithoutFileMultiMap.get("title").get(0))
                    .multiPart("icon", formParamsWithoutFileMultiMap.get("icon").get(0))
                    .multiPart("color", formParamsWithoutFileMultiMap.get("color").get(0))
                    .when()
                    .post("/entry")
                    .then()
                    .statusCode(500)
                    .body(Matchers.equalTo("Unexpected Error."));
        }

        private File createImageFile(String fileName, String formatName) throws IOException {
            int width = 250;
            int height = 250;
            // Create a BufferedImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // Draw a black rectangle on the image
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(50, 50, 150, 150);
            g2d.dispose();
            // Save the BufferedImage as a specified file
            File imageFile = new File(fileName);
            ImageIO.write(bufferedImage, formatName, imageFile);

            return imageFile;
        }

    }


}
