package com.wave.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// We authenticate with our own JWT flow, so Spring Boot's default in-memory user
// (and its generated password in the log) is switched off.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class WaveDsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaveDsApplication.class, args);
    }
}
