package com.xebia.keycloak.domain

import com.xebia.keycloak.domain.model.Skill

interface SkillsManagementSpi {
    fun getAll(): List<Skill>
}
