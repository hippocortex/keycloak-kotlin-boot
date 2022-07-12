package com.xebia.keycloak.application.rest.model

import com.xebia.keycloak.domain.model.Skill

data class SkillDto(
    val id: Int,
    val skill: String,
    val category: String
) {
    companion object {
        fun fromDomain(skill: Skill): SkillDto {
            return SkillDto(id = skill.id, skill = skill.type, category = skill.category)
        }
    }
}