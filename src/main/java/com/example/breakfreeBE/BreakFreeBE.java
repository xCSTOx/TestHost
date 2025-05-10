package com.example.breakfreeBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.breakfreeBE.userRegistration.entity",
        "com.example.breakfreeBE.avatar.entity",
        "com.example.breakfreeBE.achievement.entity",
        "com.example.breakfreeBE.addiction.entity",
        "com.example.breakfreeBE.challenge.entity"
})
@ComponentScan(basePackages = "com.example.breakfreeBE")
@EnableJpaRepositories(basePackages = {
        "com.example.breakfreeBE.achievement.repository",
        "com.example.breakfreeBE.addiction.repository",
        "com.example.breakfreeBE.challenge.repository",
        "com.example.breakfreeBE.userRegistration.repository",
        "com.example.breakfreeBE.avatar.repository"
})

public class BreakFreeBE {

    public static void main(String[] args) {
        SpringApplication.run(BreakFreeBE.class, args);
    }
}
