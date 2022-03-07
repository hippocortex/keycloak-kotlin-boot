package com.xebia.keycloak.client

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OauthServicesApplicationTests {

	@Autowired
	lateinit var serviceAdapter: ServiceAdapter
	@Test
	fun contextLoads() {
	}


	@Test
	fun shouldLogin(){
		val token = serviceAdapter.getToken()
		val result = serviceAdapter.getSkills()
		print(token)
		print(result)
	}


}
