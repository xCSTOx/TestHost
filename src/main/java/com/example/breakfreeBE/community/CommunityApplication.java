//package com.example.breakfreeBE.community;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@SpringBootApplication
//@EntityScan(basePackages = {
//        "com.example.breakfreeBE.community.entity",
//        "com.example.breakfreeBE.userRegistration.entity",
//        "com.example.breakfreeBE.addiction.entity"
//})
//@ComponentScan(basePackages = {
//        "com.example.breakfreeBE.community",
//        "com.example.breakfreeBE.userRegistration" // supaya service dan component-nya ikut discan
//})
//@EnableJpaRepositories(basePackages = {
//        "com.example.breakfreeBE.community.repository",
//        "com.example.breakfreeBE.userRegistration.repository"
//})
//public class CommunityApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(CommunityApplication.class, args);
//    }
//}