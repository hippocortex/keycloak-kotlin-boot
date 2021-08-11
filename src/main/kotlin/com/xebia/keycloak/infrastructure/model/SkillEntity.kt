package com.xebia.keycloak.infrastructure.model

import com.xebia.keycloak.domain.model.Skill

data class SkillEntity(
    val id: Int,
    val name: String,
    val category: String
) {
    fun
        toDomain(): Skill {
        return Skill(
            id = id,
            name = name,
            category = category
        )
    }
}
