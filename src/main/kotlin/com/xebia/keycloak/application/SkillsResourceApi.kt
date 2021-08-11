package com.xebia.keycloak.application

import com.xebia.keycloak.application.error.ErrorHandlerController.ErrorDto
import com.xebia.keycloak.application.model.SkillDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/skills/V1/public")
interface SkillsResourceApi {

    @GetMapping(path = ["/skills"])
    @Operation(summary = "Find the authenticated user by jwt attribute userId")
    @ApiResponses(
        ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = SkillDto::class))]),
        ApiResponse(responseCode = "404", description = "User not Found", content = [Content(schema = Schema(implementation = ErrorDto::class))]),
        ApiResponse(responseCode = "403", description = "Forbidden"),
        ApiResponse(responseCode = "401", description = "Unauthorized"),
        ApiResponse(responseCode = "500", description = "Internal Server error")
    )
    fun getSkills(): List<SkillDto>
}