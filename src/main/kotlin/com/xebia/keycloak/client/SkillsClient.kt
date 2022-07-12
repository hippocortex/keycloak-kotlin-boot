package com.xebia.keycloak.client

import com.xebia.keycloak.domain.model.Skill
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface SkillsClient {
    @Headers("Content-Type: application/json")
    @GET("/skills/v1/public/view")
    fun getSkills(): Call<List<Skill>>
}