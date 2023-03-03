package br.com.erudio.integrationtests.testcontainers.controllers.yaml;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.testcontainers.controllers.yaml.mapper.YAMLMapper;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YAMLMapper yamlMapper;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup() {
        yamlMapper = new YAMLMapper();
    }


    @Test
    @Order(1)
    void testSignin() throws JsonProcessingException {
        var user = new AccountCredentialsVO("leandro", "admin123");
        tokenVO = given()
                .config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().encodeContentTypeAs(
                                        TestConfigs.CONTENT_TYPE_YAML,
                                        ContentType.TEXT
                                )
                        )
                )
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .body(user, yamlMapper)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(TokenVO.class, yamlMapper);

        assertNotNull(tokenVO);
        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() throws JsonProcessingException {
        var user = new AccountCredentialsVO("leandro", "admin123");
        var newTokenVO = given()
                .config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().encodeContentTypeAs(
                                        TestConfigs.CONTENT_TYPE_YAML,
                                        ContentType.TEXT
                                )
                        )
                )
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .pathParams("username", tokenVO.getUserName())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(TokenVO.class, yamlMapper);

        assertNotNull(newTokenVO);
        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
