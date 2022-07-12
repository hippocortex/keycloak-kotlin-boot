package com.xebia.keycloak.utils

import io.restassured.response.ValidatableResponse
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode


fun ValidatableResponse.assertJsonBodyIsEqualStrict(expected: String) {
    try {
        JSONAssert.assertEquals(expected, this.extract().asString(), JSONCompareMode.STRICT)
    } catch (e: AssertionError) {
        println("Response body: ${this.extract().asString()}")
        throw e
    }
}