package com.limtic.lab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.limtic.lab.repository.UserRepository;

@SpringBootApplication
public class LabBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabBackendApplication.class, args);
	}
	@Bean
    CommandLineRunner testMongo(UserRepository userRepository) {
        return args -> {
            System.out.println("Testing MongoDB connection...");
            System.out.println("Current users count: " + userRepository.count());
        };
    }

}
