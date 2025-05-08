package com.example.breakfreeBE.addiction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.breakfreeBE.userRegistration.entity","com.example.breakfreeBE.community.entity", "com.example.breakfreeBE.addiction.entity"})
public class AddictionApplication {
    public static void main(String[] args) {
        SpringApplication.run(AddictionApplication.class, args);
    }
}