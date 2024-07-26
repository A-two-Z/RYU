package com.ssafy.mulryuproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MulryuProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MulryuProjectApplication.class, args);
	}

}
