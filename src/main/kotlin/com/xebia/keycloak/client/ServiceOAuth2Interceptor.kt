package com.xebia.keycloak.client

import okhttp3.Interceptor
import okhttp3.Response

internal class ServiceOAuth2Interceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
                .let { chain.proceed(it) }
    }
}