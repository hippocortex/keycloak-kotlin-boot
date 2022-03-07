package com.xebia.keycloak.infrastructure.model

import com.xebia.keycloak.domain.model.Skill

data class SkillEntity(
    val id: Int,
    val type: String,
    val category: String
) {
    fun toDomain(): Skill {
        return Skill(
            id = id,
            type = type,
            category = category
        )
    }
}
