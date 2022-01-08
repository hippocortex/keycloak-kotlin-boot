package com.xebia.keycloak.application

import com.xebia.keycloak.application.rest.model.SkillDto
import com.xebia.keycloak.domain.SkillsManagementApi
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*","http://127.0.0.1:8080/*"])
@RestController
class SkillsResource(val skillsManagementService: SkillsManagementApi) : SkillsResourceApi {
    override fun getSkills(): List<SkillDto> {
        return skillsManagementService.getAll().map {
            SkillDto.fromDomain(it)
        }
    }
}