package com.example.breakfreeBE.userRegistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.breakfreeBE.userRegistration.entity", "com.example.breakfreeBE.community.entity", "com.example.breakfreeBE.addiction.entity"})
public class UserRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationApplication.class, args);
    }

}
