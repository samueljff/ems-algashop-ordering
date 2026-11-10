package com.fonseca.algashop.ordering.utils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

	private final JwtAuthenticationConverter jwtAuthenticationConverter;

	public WithMockJwtSecurityContextFactory(JwtAuthenticationConverter jwtAuthenticationConverter) {
		this.jwtAuthenticationConverter = jwtAuthenticationConverter;
	}

	@Override
	public SecurityContext createSecurityContext(WithMockJwt annotation) {
		Jwt jwt = MockJwtFactory.buildJwt(
				"mock-value",
				annotation.subject(),
				MockJwtFactory.DEFAULT_ISSUER_URI,
				annotation.scopes(),
				annotation.role(),
				annotation.audiences()
		);

		AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwt);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authenticationToken);

		return context;
	}
}