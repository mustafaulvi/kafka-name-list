package com.example.userstorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.shared.model"})
@EnableJpaRepositories(basePackages = {"com.example.userstorageservice.repository"})
public class UserStorageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStorageServiceApplication.class, args);
    }
} 