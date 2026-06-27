package com.fonseca.algashop.ordering.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestcontainerPostgreSQLConfig {

    static {
        System.setProperty("api.version", "1.44");
    }

    private static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:17-alpine");

    @Bean
    @ServiceConnection
    public PostgreSQLContainer postgreSQLContainer() {
        return postgreSQLContainer;
    }

}