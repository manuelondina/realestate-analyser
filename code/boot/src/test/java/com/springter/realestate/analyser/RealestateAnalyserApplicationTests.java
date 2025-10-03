package com.springter.realestate.analyser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
				"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration," +
				"org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration"
})
class RealestateAnalyserApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring Boot application context loads successfully
		// with all modules properly wired together
	}

}