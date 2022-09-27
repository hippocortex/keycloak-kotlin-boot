package com.xebia.keycloak.infrastructure

import com.xebia.keycloak.domain.SkillsManagementSpi
import com.xebia.keycloak.domain.model.Skill
import com.xebia.keycloak.infrastructure.model.SkillEntity
import org.springframework.stereotype.Component

@Component
class SkillManagementAdapter : SkillsManagementSpi {

    companion object {
        val skills = mapOf<Int, SkillEntity>(
            1 to SkillEntity(id = 1, name = "Java", category = "BackEnd"),
            2 to SkillEntity(id = 2, name = "Python", category = "BackEnd")
        )
    }

    override fun getAll(): List<Skill> {
        return ArrayList(
            skills.values.map {
                it.toDomain()
            }
        )
    }
}