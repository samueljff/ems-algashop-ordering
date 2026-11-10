package com.fonseca.algashop.ordering.core.application;

import com.fonseca.algashop.ordering.utils.MockJwtDecoderConfig;
import com.fonseca.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import com.fonseca.algashop.ordering.utils.WithMockJwt;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerPostgreSQLConfig.class, MockJwtDecoderConfig.class})
@WithMockJwt
public abstract class AbstractApplicationIT {

}