package com.xebia.keycloak.application

import com.xebia.keycloak.application.rest.model.SkillDto
import com.xebia.keycloak.domain.SkillsManagementApi
import org.springframework.web.bind.annotation.RestController

@RestController
class SkillsResource(val skillsManagementService: SkillsManagementApi) : SkillsResourceApi {
    override fun getSkills(): List<SkillDto> {
        return skillsManagementService.getAll().map {
            SkillDto.fromDomain(it)
        }
    }
}