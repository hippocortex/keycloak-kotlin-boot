package com.xebia.keycloak.application.configuration

import org.slf4j.MDC
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*

@Configuration
class MdcAndLogUserIdConfig {
    @Bean
    fun removeMdcFilter(): FilterRegistrationBean<MdcClearWebFilter> {
        val filter: FilterRegistrationBean<MdcClearWebFilter> = FilterRegistrationBean<MdcClearWebFilter>()
        filter.filter = MdcClearWebFilter()
        filter.addUrlPatterns("/*")
        filter.setName("MdcClearFilter")
        filter.order = Ordered.HIGHEST_PRECEDENCE // Must be the first to keep MDC until the end of the request !!!!
        return filter
    }

    @Component
    class AuthenticationSuccessListener {
        /**
         * By using the [EventListener] annotation, each time a token is successfully validated, we add the userId
         * to all the logs.
         */
        @EventListener
        fun handleAuthenticationSuccess(event: AuthenticationSuccessEvent) {
            MDC.put(
                    "keycloakId",
                    ((event.source as JwtAuthenticationToken).principal as Jwt).getClaimAsString("sub")
            )
        }
    }

    class MdcClearWebFilter : Filter {
        override fun init(filterConfig: FilterConfig) {
            // Not used
        }

        @Throws(IOException::class, ServletException::class)
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            try {
                chain.doFilter(request, response)
            } finally {
                MDC.clear()
            }
        }

        override fun destroy() {
            // Not used
        }
    }
}