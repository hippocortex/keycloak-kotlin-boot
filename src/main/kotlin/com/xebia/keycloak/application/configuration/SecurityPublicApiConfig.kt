package com.xebia.keycloak.application.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@EnableWebSecurity
@Configuration
@Order(1)
class SecurityPublicApiConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/skills/v1/view/**").hasRole("consult")
            .antMatchers(
                "/actuator/**",
                "/error",
                "/skills/v1/swagger-ui.html",
                "/skills/v1/api-docs/**",
                "/skills/v1/api-docs.yaml",
                "/skills/v1/swagger-ui/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt().jwtAuthenticationConverter(getJwtAuthenticationConverter())
    }

    private fun getJwtAuthenticationConverter(): Converter<Jwt, AbstractAuthenticationToken> {
        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter(
                KeyCloakGrantedAuthoritiesConverter()
            )
        }
    }
}