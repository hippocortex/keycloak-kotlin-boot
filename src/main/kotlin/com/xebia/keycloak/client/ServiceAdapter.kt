package com.xebia.keycloak.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.xebia.keycloak.domain.model.Skill
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Component
class ServiceAdapter() {

    val skillsBaseUri = "http://localhost:8080"
    var apiToken: String = ""
    val authBaseUri = "http://localhost:9595/"

    fun getToken(): String {
        apiToken = authenticationClient().login(clientId = "appspringoauth", clientSecret = "QszH5s5svkcwLmT0GZtIYhnWCSqWG782", realm = "microservices").execute().body()!!.accessToken
        return apiToken
    }

    fun getSkills(): List<Skill> {
        return skillsClient().getSkills().execute().body()!!

    }

    fun authenticationClient(): AuthenticationClient {
        val okHttpClient: okhttp3.OkHttpClient = okhttp3.OkHttpClient.Builder()
            .build()


        return Retrofit.Builder()
            .baseUrl(authBaseUri)
            .addConverterFactory(
                JacksonConverterFactory.create(createJacksonKotlinMapper())
            )
            .client(okHttpClient)
            .build()
            .create(AuthenticationClient::class.java)
    }


    fun skillsClient(): SkillsClient {
        val okHttpClient: okhttp3.OkHttpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(ServiceOAuth2Interceptor(apiToken))
            .build()


        return Retrofit.Builder()
            .baseUrl(skillsBaseUri)
            .addConverterFactory(
                JacksonConverterFactory.create(createJacksonKotlinMapper())
            )
            .client(okHttpClient)
            .build()
            .create(SkillsClient::class.java)
    }

    private fun createJacksonKotlinMapper(): ObjectMapper {
        return jacksonObjectMapper().apply {
            registerKotlinModule()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
}