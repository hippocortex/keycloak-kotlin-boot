package com.xebia.keycloak.infrastructure

import com.xebia.keycloak.domain.SkillsManagementSpi
import com.xebia.keycloak.domain.model.Skill
import com.xebia.keycloak.infrastructure.model.SkillEntity
import org.springframework.stereotype.Component

@Component
class SkillManagementAdapter : SkillsManagementSpi {

    companion object {
        val skills = listOf(
            SkillEntity(id = 1, type = "Java", category = "BackEnd"),
            SkillEntity(id = 2, type = "Python", category = "BackEnd")
        )
    }

    override fun getAll(): List<Skill> {
        return ArrayList(
            skills.map {
                it.toDomain()
            }
        )
    }
}