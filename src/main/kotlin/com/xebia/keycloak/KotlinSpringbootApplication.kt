package com.xebia.keycloak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan

class KotlinSpringbootApplication

fun main(args: Array<String>) {
	runApplication<KotlinSpringbootApplication>(*args)
}
