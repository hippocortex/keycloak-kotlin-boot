package com.xebia.keycloak.application.rest.model

import com.xebia.keycloak.domain.model.Skill

data class SkillDto(
    val id: Int,
    val type: String,
    val category: String
) {
    companion object {
        fun fromDomain(skill: Skill): SkillDto {
            return SkillDto(id = skill.id, type = skill.type, category = skill.category)
        }
    }
}