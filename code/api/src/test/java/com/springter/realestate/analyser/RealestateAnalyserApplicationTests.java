package com.springter.realestate.analyser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class, 
		HibernateJpaAutoConfiguration.class,
		OpenAiAutoConfiguration.class
})
class RealestateAnalyserApplicationTests {

	@Test
	void contextLoads() {
	}

}
