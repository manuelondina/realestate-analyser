package com.springter.realestate.analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealestateAnalyserApplication {

	public static void main(String[] args) {
		System.out.println("\n" +
				"🏠 ================================================== 🏠\n" +
				"   Welcome to Real Estate Analyser Application!\n" +
				"   AI-Powered Real Estate Comparison Tool\n" +
				"   Starting application...\n" +
				"🏠 ================================================== 🏠\n");
		
		SpringApplication.run(RealestateAnalyserApplication.class, args);
		
		System.out.println("\n" +
				"✅ ================================================== ✅\n" +
				"   Real Estate Analyser Application Started Successfully!\n" +
				"   Application is ready to receive requests\n" +
				"✅ ================================================== ✅\n");
	}

}
