package com.heng.property_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PropertyManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyManagerApplication.class, args);
    }

}
