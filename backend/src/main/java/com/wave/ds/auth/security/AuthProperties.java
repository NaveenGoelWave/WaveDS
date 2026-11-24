package com.wave.ds.auth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Bound from the app.auth.* settings in application.yml. */
@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(String jwtSecret, long jwtExpirationMinutes, boolean signupEnabled) {
}
