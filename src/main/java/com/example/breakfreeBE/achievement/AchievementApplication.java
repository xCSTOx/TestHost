package com.example.breakfreeBE.achievement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.breakfreeBE.userRegistration.entity",
        "com.example.breakfreeBE.community.entity",
        "com.example.breakfreeBE.addiction.entity",
        "com.example.breakfreeBE.achievement.entity",
        "com.example.breakfreeBE.challenge.entity"
})

@ComponentScan(basePackages = {
        "com.example.breakfreeBE.achievement",
        "com.example.breakfreeBE.challenge",
        "com.example.breakfreeBE.common",
        "com.example.breakfreeBE.exception"
})

public class AchievementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AchievementApplication.class, args);
    }
}
