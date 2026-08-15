package com.example.AsyncFlow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ValidationWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidationWorkerApplication.class, args);
	}

}