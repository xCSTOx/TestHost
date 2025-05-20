package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.addiction",
        "com.example.community",
        "com.example.common",
        "com.example.userregistration",
        "com.example.achievement",
        "com.example.security"
})
@EntityScan(basePackages = {
        "com.example.addiction.entity",
        " com.example.community.entity",
        " com.example.achievement.entity",
        "com.example.userregistration.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.example.addiction.repository",
        "com.example.community.repository",
        "com.example.achievement.repository",
        "com.example.userregistration.repository"
})
public class BreakFreeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BreakFreeApplication.class, args);
    }
}

