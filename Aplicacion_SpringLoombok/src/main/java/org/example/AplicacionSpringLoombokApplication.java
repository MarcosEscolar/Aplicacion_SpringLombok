package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example", "console", "model", "service"})
public class AplicacionSpringLoombokApplication {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionSpringLoombokApplication.class, args);
    }
}