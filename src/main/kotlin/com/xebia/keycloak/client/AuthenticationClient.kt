package com.xebia.keycloak.client

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthenticationClient {

    @FormUrlEncoded
    @POST("auth/realms/{realm}/protocol/openid-connect/token")
    fun login(
            @Path("realm") realm: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType: String = "client_credentials",
    ): Call<Token>
}