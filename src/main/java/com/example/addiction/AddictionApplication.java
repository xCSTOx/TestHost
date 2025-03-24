package com.example.addiction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.userregistration.entity", "com.example.addiction.entity"})
public class AddictionApplication {
    public static void main(String[] args) {
        SpringApplication.run(AddictionApplication.class, args);
    }
}