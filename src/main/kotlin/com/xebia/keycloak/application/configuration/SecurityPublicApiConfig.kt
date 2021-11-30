package com.xebia.keycloak.application.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@EnableWebSecurity
@Configuration
class SecurityPublicApiConfig : WebSecurityConfigurerAdapter() {



    override fun configure(http: HttpSecurity) {
    http
    .csrf().disable()
    .cors().disable()
    .formLogin().disable()
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
    .authorizeRequests()
    .antMatchers("/skills/v1/public/**").hasRole("payment")
    .antMatchers(
    "/actuator/**",
    "/error",

    "/skills/v1/api-docs/**",

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