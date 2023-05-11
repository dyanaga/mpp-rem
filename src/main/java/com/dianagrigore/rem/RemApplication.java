package com.dianagrigore.rem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RemApplication.class, args);
    }

}
