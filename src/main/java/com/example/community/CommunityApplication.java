package com.example.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.community.entity",
        "com.example.userregistration.entity",
        "com.example.addiction.entity"
})
@ComponentScan(basePackages = {
        "com.example.community",
        "com.example.userregistration" // supaya service dan component-nya ikut discan
})
@EnableJpaRepositories(basePackages = {
        "com.example.community.repository",
        "com.example.userregistration.repository"
})
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}