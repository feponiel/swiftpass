package com.feponiel.swiftpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SwiftpassApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftpassApplication.class, args);
	}

}
