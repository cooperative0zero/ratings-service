package com.modsen.software.ratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RatingsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingsServiceApplication.class, args);
	}

}
