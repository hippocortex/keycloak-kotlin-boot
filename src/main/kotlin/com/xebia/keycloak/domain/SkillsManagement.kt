package com.xebia.keycloak.domain

import com.xebia.keycloak.domain.model.Skill
import org.springframework.stereotype.Service

@Service
class SkillsManagement(private val skillsManagementSpi:SkillsManagementSpi):SkillsManagementApi {
    override fun getAll(): List<Skill> {
        return skillsManagementSpi.getAll()
    }
}