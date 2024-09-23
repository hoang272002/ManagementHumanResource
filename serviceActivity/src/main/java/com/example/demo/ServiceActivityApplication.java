package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "model", "controller"})
@EnableMongoRepositories(basePackages = "repository")

public class ServiceActivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceActivityApplication.class, args);
	}

}
