package org.divyad.config;

import org.divyad.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class AADWebSecurityConfig {

    @Autowired
    AuthorizationFilter oAuth2Filter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                .authorizeExchange(authz -> authz
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/h2/**").permitAll()
                        .anyExchange().authenticated()
                ).oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(jwtSpec -> {
                    return;
                }));

        return http.build();
    }
}
