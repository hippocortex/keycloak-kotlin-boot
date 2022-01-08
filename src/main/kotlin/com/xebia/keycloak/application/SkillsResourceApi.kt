package com.xebia.keycloak.application

import com.xebia.keycloak.application.rest.configuration.OpenApiConfig
import com.xebia.keycloak.application.rest.model.SkillDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/skills/v1/public")
@SecurityRequirements(value = [SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)])
interface SkillsResourceApi {

    @GetMapping(path = ["/view"])

    fun getSkills(): List<SkillDto>
}