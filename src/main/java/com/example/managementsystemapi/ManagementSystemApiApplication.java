package com.example.managementsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ManagementSystemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementSystemApiApplication.class, args);
    }

}
