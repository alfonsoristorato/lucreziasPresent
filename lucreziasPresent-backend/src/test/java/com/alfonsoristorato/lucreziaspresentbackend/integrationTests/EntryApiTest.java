package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
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
    @DisplayName("Entry endpoint:: GET /entry")
    @Order(1)
    class findAllEntriesTest {
        @Test
        @DisplayName("GET /entry")
        void findAllEntries_returns200AndAnOrderedByDateListOfEntries() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .get("/entry")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(2),
                            "[0].id", equalTo(2),
                            "[1].id", equalTo(1),
                            "[0].date", equalTo("2023-02-10"),
                            "[1].date", equalTo("2023-10-10"));
        }
    }

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
        @DisplayName("POST /entry without form")
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
        @DisplayName("POST /entry with form and non-supported file")
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
        @DisplayName("POST /entry with incomplete form")
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

        @Test
        @DisplayName("POST /entry with complete and incorrect form")
        void addEntry_returns500IfFormPassedIsCompleteButIncorrect() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", formParamsWithoutFileMultiMap.get("name").get(0))
                    .multiPart("content", formParamsWithoutFileMultiMap.get("content").get(0))
                    .multiPart("title", formParamsWithoutFileMultiMap.get("title").get(0))
                    .multiPart("icon", "notAnInteger")
                    .multiPart("color", formParamsWithoutFileMultiMap.get("color").get(0))
                    .multiPart("date", formParamsWithoutFileMultiMap.get("date").get(0))
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

    @Nested
    @DisplayName("Entry endpoint:: PATCH /entry/{entryId}")
    @Order(2)
    class editEntryTest {
        @Test
        @DisplayName("PATCH /entry/{entryId} with complete and correct form and patching an entry that belongs to the logged in user")
        void editEntry_returns204IfCorrectAndCompleteFormIsPassed() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", "newName")
                    .multiPart("content", "newContent")
                    .multiPart("title", "newTitle")
                    .multiPart("icon", "5")
                    .multiPart("color", "green")
                    .multiPart("date", "2023-02-05")
                    .patch("/entry/{entryId}", 1)
                    .then()
                    .statusCode(204)
                    .body(Matchers.blankOrNullString());
        }

        @Test
        @DisplayName("PATCH /entry/{entryId} with complete and correct form and patching an entry that does not belong to the logged in user")
        void editEntry_returns403IfCorrectFormIsPassedButPathParamsLeadToAnotherUserEntry() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", "newName")
                    .multiPart("content", "newContent")
                    .multiPart("title", "newTitle")
                    .multiPart("icon", "5")
                    .multiPart("color", "green")
                    .multiPart("date", "2023-02-05")
                    .patch("/entry/{entryId}", 2)
                    .then()
                    .statusCode(403)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("Entry error"),
                            "details", equalTo("This entry belongs to another user.")
                    );
        }

        @Test
        @DisplayName("PATCH /entry/{entryId} with incomplete and correct form and patching an entry that belongs to the logged in user")
        void editEntry_returns500IfIncompleteFormIsPassed() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("content", "newContent")
                    .multiPart("title", "newTitle")
                    .multiPart("icon", "5")
                    .multiPart("color", "green")
                    .multiPart("date", "2023-02-05")
                    .patch("/entry/{entryId}", 1)
                    .then()
                    .statusCode(500)
                    .body(equalTo("Unexpected Error.")
                    );
        }

        @Test
        @DisplayName("PATCH /entry/{entryId} with complete and incorrect form and patching an entry that belongs to the logged in user")
        void editEntry_returns500IfCompleteAndIncorrectFormIsPassed() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", "newName")
                    .multiPart("content", "newContent")
                    .multiPart("title", "newTitle")
                    .multiPart("icon", "notAnInteger")
                    .multiPart("color", "green")
                    .multiPart("date", "2023-02-05")
                    .patch("/entry/{entryId}", 1)
                    .then()
                    .statusCode(500)
                    .body(equalTo("Unexpected Error.")
                    );
        }

        @Test
        @DisplayName("PATCH /entry/{entryId} with complete and correct form and patching an entry that does not exist")
        void editEntry_returns404IfEntryDoesNotExist() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("name", "newName")
                    .multiPart("content", "newContent")
                    .multiPart("title", "newTitle")
                    .multiPart("icon", "5")
                    .multiPart("color", "green")
                    .multiPart("date", "2023-02-05")
                    .patch("/entry/{entryId}", 3)
                    .then()
                    .statusCode(404)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("Entry error"),
                            "details", equalTo("Entry not found.")
                    );
        }
    }

    @Nested
    @DisplayName("Entry endpoint:: DELETE /entry/{entryId}")
    @Order(3)
    class deleteEntryTest {
        @Test
        @DisplayName("DELETE /entry/{entryId} deleting an entry that belongs to the logged in user")
        void deleteEntry_returns204IfEntryBelongsToLoggedInUser() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .delete("/entry/{entryId}", 1)
                    .then()
                    .statusCode(204)
                    .body(Matchers.blankOrNullString());
        }

        @Test
        @DisplayName("DELETE /entry/{entryId} deleting an entry that does not belong to the logged in user")
        void editEntry_returns403IfEntryDoesNotBelongToLoggedInUser() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .delete("/entry/{entryId}", 2)
                    .then()
                    .statusCode(403)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("Entry error"),
                            "details", equalTo("This entry belongs to another user.")
                    );
        }

        @Test
        @DisplayName("DELETE /entry/{entryId} deleting an entry that does not exist")
        void editEntry_returns404IfEntryDoesNotExist() {
            client.request()
                    .auth()
                    .basic("user", "defaultPass")
                    .when()
                    .delete("/entry/{entryId}", 9999)
                    .then()
                    .statusCode(404)
                    .body(
                            "size()", equalTo(2),
                            "description", equalTo("Entry error"),
                            "details", equalTo("Entry not found.")
                    );
        }
    }


}
