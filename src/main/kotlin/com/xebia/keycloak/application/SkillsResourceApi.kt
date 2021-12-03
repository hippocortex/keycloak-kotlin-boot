package com.xebia.keycloak.application

import com.xebia.keycloak.application.model.SkillDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/skills/v1/public")
interface SkillsResourceApi {

    @GetMapping(path = ["/view"])

    fun getSkills(): List<SkillDto>
}