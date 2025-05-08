package com.example.breakfreeBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.breakfreeBE")
public class BreakFreeBE {

	public static void main(String[] args) {
		SpringApplication.run(BreakFreeBE.class, args);
	}


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            for (String name : ctx.getBeanDefinitionNames()) {
                if (name.toLowerCase().contains("achievement")) {
                    System.out.println(">> Bean: " + name);
                }
            }
        };
    }
}
