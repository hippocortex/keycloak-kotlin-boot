package com.xebia.keycloak

import com.github.tomakehurst.wiremock.client.WireMock.*
import io.restassured.RestAssured
import org.jose4j.jwk.JsonWebKeySet
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.junit.jupiter.api.BeforeEach
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.List
import java.util.Map

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(value = ["it"])
@EnableConfigurationProperties(
    KeycloakSpringBootProperties::class
)
@AutoConfigureWireMock(port = 0) //random port, that is wired into properties with key wiremock.server.port
abstract class AbstractSpringIT {


    @LocalServerPort
    var serverPort = 0


    lateinit var rsaJsonWebKey: RsaJsonWebKey

    private var testSetupIsCompleted = false

    @Value("\${wiremock.server.baseUrl}")
    private val keycloakBaseUrl: String? = null

    @Value("\${keycloak.realm}")
    private val keycloakRealm: String? = null

    @BeforeEach
    fun init() {
        RestAssured.port = serverPort
        if (!testSetupIsCompleted) {
            // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048)
            rsaJsonWebKey.keyId = "k1"
            rsaJsonWebKey.algorithm = AlgorithmIdentifiers.RSA_USING_SHA256
            rsaJsonWebKey.use = "sig"
            val openidConfig = """{
  "issuer": "$keycloakBaseUrl/auth/realms/$keycloakRealm",
  "authorization_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/auth",
  "token_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/token",
  "token_introspection_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/token/introspect",
  "userinfo_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/userinfo",
  "end_session_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/logout",
  "jwks_uri": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/certs",
  "check_session_iframe": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/login-status-iframe.html",
  "registration_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/clients-registrations/openid-connect",
  "introspection_endpoint": "$keycloakBaseUrl/auth/realms/$keycloakRealm/protocol/openid-connect/token/introspect"
}"""
            stubFor(
                get(
                    urlEqualTo(
                        String.format(
                            "/auth/realms/%s/.well-known/openid-configuration",
                            keycloakRealm
                        )
                    )
                )
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(openidConfig)
                    )
            )
            stubFor(
                get(urlEqualTo(String.format("/auth/realms/%s/protocol/openid-connect/certs", keycloakRealm)))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(JsonWebKeySet(rsaJsonWebKey).toJson())
                    )
            )
            testSetupIsCompleted = true
        }
        //wireMockServer.resetAll()


    }


    fun generateJWT(withTenantClaim: Boolean): String? {

        // Create the Claims, which will be the content of the JWT
        val claims = JwtClaims()
        claims.jwtId = UUID.randomUUID().toString() // a unique identifier for the token
        claims.setExpirationTimeMinutesInTheFuture(10F) // time when the token will expire (10 minutes from now)
        claims.setNotBeforeMinutesInThePast(0F) // time before which the token is not yet valid (2 minutes ago)
        claims.setIssuedAtToNow() // when the token was issued/created (now)
        claims.setAudience("account") // to whom this token is intended to be sent
        claims.issuer = java.lang.String.format(
            "%s/auth/realms/%s",
            keycloakBaseUrl,
            keycloakRealm
        ) // who creates the token and signs it
        claims.subject = UUID.randomUUID().toString() // the subject/principal is whom the token is about
        claims.setClaim("typ", "Bearer") // set type of token
        claims.setClaim("azp", "appspringoauth") // Authorized party  (the party to which this token was issued)
        claims.setClaim(
            "auth_time",
            NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli()).value
        ) // time when authentication occured
        claims.setClaim("session_state", UUID.randomUUID().toString()) // keycloak specific ???
        claims.setClaim("acr", "0") //Authentication context class
        claims.setClaim(
            "realm_access",
            Map.of("roles", List.of("offline_access", "uma_authorization", "admin"))
        ) //keycloak roles
        claims.setClaim(
            "resource_access", Map.of(
                "account",
                Map.of("roles", List.of("manage-account", "manage-account-links", "view-profile"))
            )
        ) //keycloak roles
        claims.setClaim("scope", "profile email")
        claims.setClaim("email_verified", true)
        claims.setClaim("preferred_username", "service-account-spring-api")


        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        val jws = JsonWebSignature()

        // The payload of the JWS is JSON content of the JWT Claims
        jws.payload = claims.toJson()

        // The JWT is signed using the private key
        jws.key = rsaJsonWebKey.privateKey

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.keyIdHeaderValue = rsaJsonWebKey.keyId

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256

        // set the type header
        jws.setHeader("typ", "JWT")

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        return jws.compactSerialization
    }


}