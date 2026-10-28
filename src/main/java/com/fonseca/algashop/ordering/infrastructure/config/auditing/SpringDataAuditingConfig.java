package com.fonseca.algashop.ordering.infrastructure.config.auditing;

import com.fonseca.algashop.ordering.core.application.security.SecurityCheckApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider", auditorAwareRef = "auditorProvider")
public class SpringDataAuditingConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }

    @Bean
    public AuditorAware<UUID> auditorProvider(SecurityCheckApplicationService securityCheck) {
        return () -> {
            if (!securityCheck.isAuthenticated() || securityCheck.isMachineAuthenticated()) {
                return Optional.empty();
            }
            return Optional.of(securityCheck.getAuthenticatedUserId());
        };
    }
}
