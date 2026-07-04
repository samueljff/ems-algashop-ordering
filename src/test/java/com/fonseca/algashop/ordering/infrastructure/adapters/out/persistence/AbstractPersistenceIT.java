package com.fonseca.algashop.ordering.infrastructure.adapters.out.persistence;

import com.fonseca.algashop.ordering.infrastructure.config.auditing.SpringDataAuditingConfig;
import com.fonseca.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerPostgreSQLConfig.class, SpringDataAuditingConfig.class})
public abstract class AbstractPersistenceIT {

}