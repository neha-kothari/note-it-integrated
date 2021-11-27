package com.noteit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class NoteItApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItApplication.class, args);
        System.out.println("Hello!");
    }
}
