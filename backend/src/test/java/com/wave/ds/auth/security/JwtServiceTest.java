package com.wave.ds.auth.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wave.ds.auth.entity.Role;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "unit-test-secret-unit-test-secret-1234";

    private JwtService service(String secret, long minutes) {
        return new JwtService(new AuthProperties(secret, minutes, true));
    }

    @Test
    void generatedTokenCanBeParsedBack() {
        JwtService service = service(SECRET, 60);

        String token = service.generate(5L, "a@b.com", Role.ADMIN);

        assertThat(service.parse(token)).contains(new AuthUser(5L, "a@b.com", Role.ADMIN));
        assertThat(service.expiresInSeconds()).isEqualTo(3600);
    }

    @Test
    void tamperedTokenIsRejected() {
        JwtService service = service(SECRET, 60);
        String token = service.generate(5L, "a@b.com", Role.USER);

        assertThat(service.parse(token + "x")).isEmpty();
    }

    @Test
    void tokenSignedWithAnotherSecretIsRejected() {
        String token = service(SECRET, 60).generate(5L, "a@b.com", Role.USER);

        assertThat(service("a-completely-different-secret-1234567890", 60).parse(token)).isEmpty();
    }

    @Test
    void expiredTokenIsRejected() {
        JwtService service = service(SECRET, -1);
        String token = service.generate(5L, "a@b.com", Role.USER);

        assertThat(service.parse(token)).isEmpty();
    }

    @Test
    void garbageIsRejected() {
        JwtService service = service(SECRET, 60);

        assertThat(service.parse("not-a-jwt")).isEmpty();
        assertThat(service.parse("")).isEmpty();
    }

    @Test
    void shortSecretFailsFast() {
        assertThatThrownBy(() -> service("too-short", 60)).isInstanceOf(IllegalStateException.class);
    }
}
