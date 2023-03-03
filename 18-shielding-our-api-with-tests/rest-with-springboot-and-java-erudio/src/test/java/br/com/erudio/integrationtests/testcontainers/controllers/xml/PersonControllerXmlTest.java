package br.com.erudio.integrationtests.testcontainers.controllers.xml;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

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
    @Order(0)
    void authorization() throws JsonProcessingException {
        var user = new AccountCredentialsVO("leandro", "admin123");
        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(user)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body().as(TokenVO.class)
                        .getAccessToken();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
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
        assertEquals("Nelson", createdPersonVO.getFirstName());
        assertEquals("Piquet", createdPersonVO.getLastName());
        assertEquals("Brasilia - Brazil", createdPersonVO.getAddress());
        assertEquals("Male", createdPersonVO.getGender());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        personVO.setLastName("Piquet Souto Maior");

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(personVO)
                .when()
                    .put()
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
        assertEquals(personVO.getId(), createdPersonVO.getId());
        assertEquals("Nelson", createdPersonVO.getFirstName());
        assertEquals("Piquet Souto Maior", createdPersonVO.getLastName());
        assertEquals("Brasilia - Brazil", createdPersonVO.getAddress());
        assertEquals("Male", createdPersonVO.getGender());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
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
        assertEquals(personVO.getId(), createdPersonVO.getId());
        assertEquals("Nelson", createdPersonVO.getFirstName());
        assertEquals("Piquet Souto Maior", createdPersonVO.getLastName());
        assertEquals("Brasilia - Brazil", createdPersonVO.getAddress());
        assertEquals("Male", createdPersonVO.getGender());
    }

    @Test
    @Order(4)
    void testDelete() throws JsonProcessingException {
        given()
                .spec(requestSpecification)
                .pathParams("id", personVO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(5)
    void testFindAll() throws JsonProcessingException {
        var content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(personVO)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        var people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {});
        var foundPersonVO = people.get(0);
        personVO = foundPersonVO;

        assertNotNull(foundPersonVO.getId());
        assertNotNull(foundPersonVO.getFirstName());
        assertNotNull(foundPersonVO.getLastName());
        assertNotNull(foundPersonVO.getAddress());
        assertNotNull(foundPersonVO.getGender());
        assertEquals(3, foundPersonVO.getId());
        assertEquals("Leonardo", foundPersonVO.getFirstName());
        assertEquals("daVinci", foundPersonVO.getLastName());
        assertEquals("Anchiano - Italy", foundPersonVO.getAddress());
        assertEquals("Male", foundPersonVO.getGender());
    }

    @Test
    @Order(6)
    void testFindAllWithoutToken() throws JsonProcessingException {
        var specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given()
                .spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(personVO)
                .when()
                    .get()
                .then()
                    .statusCode(403);
    }

    private void mockPerson() {
        personVO.setFirstName("Nelson");
        personVO.setLastName("Piquet");
        personVO.setAddress("Brasilia - Brazil");
        personVO.setGender("Male");
    }
}
