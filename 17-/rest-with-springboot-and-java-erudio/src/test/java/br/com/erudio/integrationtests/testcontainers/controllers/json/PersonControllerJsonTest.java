package br.com.erudio.integrationtests.testcontainers.controllers.json;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;

    private static PersonVO personVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personVO = new PersonVO();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personVO)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        var createdPersonVO = objectMapper.readValue(content, PersonVO.class);
        personVO = createdPersonVO;

        assertNotNull(createdPersonVO);
        assertNotNull(createdPersonVO.getId());
        assertNotNull(createdPersonVO.getFirstName());
        assertNotNull(createdPersonVO.getLastName());
        assertNotNull(createdPersonVO.getAddress());
        assertNotNull(createdPersonVO.getGender());
        assertTrue(createdPersonVO.getId() > 0);
        assertEquals("Richard", createdPersonVO.getFirstName());
        assertEquals("Stallman", createdPersonVO.getLastName());
        assertEquals("New York City - New York - US", createdPersonVO.getAddress());
        assertEquals("Male", createdPersonVO.getGender());
    }

    @Test
    @Order(2)
    void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(personVO)
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockPerson();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", personVO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        var createdPersonVO = objectMapper.readValue(content, PersonVO.class);
        personVO = createdPersonVO;

        assertNotNull(createdPersonVO);
        assertNotNull(createdPersonVO.getId());
        assertNotNull(createdPersonVO.getFirstName());
        assertNotNull(createdPersonVO.getLastName());
        assertNotNull(createdPersonVO.getAddress());
        assertNotNull(createdPersonVO.getGender());
        assertTrue(createdPersonVO.getId() > 0);
        assertEquals("Richard", createdPersonVO.getFirstName());
        assertEquals("Stallman", createdPersonVO.getLastName());
        assertEquals("New York City - New York - US", createdPersonVO.getAddress());
        assertEquals("Male", createdPersonVO.getGender());
    }

    @Test
    @Order(4)
    void testFindByIdWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", personVO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        personVO.setFirstName("Richard");
        personVO.setLastName("Stallman");
        personVO.setAddress("New York City - New York - US");
        personVO.setGender("Male");
    }
}
