package com.xebia.keycloak.application.configuration

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.util.Assert

/**
 * Extracts the [GrantedAuthority]s from scope attributes for a Keycloak IAM server
 *
 * Keycloak store roles in is own way. So we have to override the Authorities Converter
 *
 * Original code:
 * @see org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
 */
class KeyCloakGrantedAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {
    private var authorityPrefix = "ROLE_"

    companion object {
        private const val KEYCLOAK_CLAIM_NAME = "realm_access"
        private const val KEYCLOAK_ROLES = "roles"
    }

    /**
     * Extract [GrantedAuthority]s from the given Keycloak [Jwt].
     *
     * @return The [authorities][GrantedAuthority] read from the token scopes
     */
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = ArrayList()
        for (authority in getAuthorities(jwt)) {
            grantedAuthorities.add(SimpleGrantedAuthority(authorityPrefix + authority))
        }
        return grantedAuthorities
    }

    /**
     * Sets the prefix to use for [authorities][GrantedAuthority] mapped by this converter.
     * Defaults to "ROLE_".
     */
    fun setAuthorityPrefix(authorityPrefix: String) {
        Assert.notNull(authorityPrefix, "authorityPrefix cannot be null")
        this.authorityPrefix = authorityPrefix
    }

    private fun getAuthorities(jwt: Jwt): Collection<String> {
        val authorities = jwt.getClaim<Any>(KEYCLOAK_CLAIM_NAME)
        if (authorities is Map<*, *> && authorities.containsKey(KEYCLOAK_ROLES)) {
            val roles = authorities[KEYCLOAK_ROLES]
            if (roles is ArrayList<*>) {
                return roles
                    .filterIsInstance(String::class.java)
            }
        }
        return listOf()
    }
}