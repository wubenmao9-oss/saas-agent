package com.benmao.saasagent;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.benmao.mapper")
class SaasAgentApplicationTests {

	@Test
	void contextLoads() {
	}
}
