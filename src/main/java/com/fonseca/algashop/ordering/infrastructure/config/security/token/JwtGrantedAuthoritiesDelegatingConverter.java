package com.fonseca.algashop.ordering.infrastructure.config.security.token;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtGrantedAuthoritiesDelegatingConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private final JwtGrantedAuthoritiesConverter scopeAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>(scopeAuthoritiesConverter.convert(jwt));

		String role = jwt.getClaimAsString("role");
		if (StringUtils.isNotBlank(role)) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}

		return grantedAuthorities;
	}
}