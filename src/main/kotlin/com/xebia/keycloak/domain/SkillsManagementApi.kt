package com.xebia.keycloak.domain

import com.xebia.keycloak.domain.model.Skill

interface SkillsManagementApi {

    fun getAll():List<Skill>
}