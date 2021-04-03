package com.amazingsoft.amazingproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.amazingsoft.amazingproject.repositories")
public class AmazingProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmazingProjectApplication.class, args);
    }

}
