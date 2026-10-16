package com.fonseca.algashop.ordering.core.domain.model;

import com.fonseca.algashop.ordering.utils.MockJwtDecoderConfig;
import com.fonseca.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerPostgreSQLConfig.class, MockJwtDecoderConfig.class})
public class AbstractRepositoryIT {
}