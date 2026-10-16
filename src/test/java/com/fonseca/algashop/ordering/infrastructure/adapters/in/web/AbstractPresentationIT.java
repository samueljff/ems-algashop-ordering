package com.fonseca.algashop.ordering.infrastructure.adapters.in.web;

import com.fonseca.algashop.ordering.utils.MockJwtDecoderConfig;
import com.fonseca.algashop.ordering.utils.MockJwtDecoderFactory;
import com.fonseca.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.config.JsonConfig.jsonConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Testcontainers
@Import({TestcontainerPostgreSQLConfig.class, MockJwtDecoderConfig.class})
public abstract class AbstractPresentationIT {

    @LocalServerPort
    protected int port;

    protected static WireMockServer wireMockProductCatalog;
    protected static WireMockServer wireMockRapidex;

    protected void beforeEach() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
    }

    protected RequestSpecification givenAuthenticated() {
        return givenAuthenticated(MockJwtDecoderFactory.DEFAULT_TOKEN_VALUE);
    }

    protected RequestSpecification givenWithExpiredToken() {
        return givenWithExpiredToken(MockJwtDecoderFactory.EXPIRED_TOKEN_VALUE);
    }

    protected RequestSpecification givenAuthenticatedWithNoScopeToken() {
        return givenAuthenticatedWithNoScopeToken(MockJwtDecoderFactory.NO_SCOPE_TOKEN_VALUE);
    }

    protected RequestSpecification givenAuthenticated(String tokenValue) {
        return RestAssured.given()
            .header("Authorization", "Bearer " + tokenValue);
    }

    protected RequestSpecification givenWithExpiredToken(String tokenValue) {
        return RestAssured.given()
            .header("Authorization", "Bearer " + tokenValue);
    }

    protected RequestSpecification givenAuthenticatedWithNoScopeToken(String tokenValue) {
        return RestAssured.given()
            .header("Authorization", "Bearer " + tokenValue);
    }

    protected static void initWireMock() {
        wireMockRapidex = new WireMockServer(options()
                .templatingEnabled(true)
                .port(8780)
                .usingFilesUnderDirectory("src/test/resources/wiremock/rapidex"));

        wireMockProductCatalog = new WireMockServer(options()
                .templatingEnabled(true)
                .port(8781)
                .usingFilesUnderDirectory("src/test/resources/wiremock/product-catalog"));

        wireMockRapidex.start();
        wireMockProductCatalog.start();
    }

    protected static void stopMock() {
        wireMockRapidex.stop();
        wireMockProductCatalog.stop();
    }
}